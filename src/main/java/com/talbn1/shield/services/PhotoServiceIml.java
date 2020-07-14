package com.talbn1.shield.services;

import com.talbn1.shield.domain.Photo;
import com.talbn1.shield.mappers.Mapper;
import com.talbn1.shield.model.PhotoDto;
import com.talbn1.shield.repositories.PhotoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * @author talbn on 7/11/2020
 **/


@Service
@AllArgsConstructor
@Slf4j
public class PhotoServiceIml implements PhotoService{

    private final Mapper photoMapper;
    private final PhotoRepository photoRepository;
    private final String DATE_CONST = "yyyy-MM-dd-HH-mm-ss";


    public static String request(String endpoint) throws IOException {
        StringBuilder sb = new StringBuilder();

        URL url = new URL(endpoint);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream inputStream = urlConnection.getInputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

            InputStreamReader inputStreamReader = new InputStreamReader(bufferedInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String inputLine = bufferedReader.readLine();
            sb.append("{\"photos\":");
            while (inputLine != null) {
                sb.append(inputLine);
                inputLine = bufferedReader.readLine();
            }

        }catch (MalformedURLException mue){
            mue.printStackTrace();
        }
        catch (IOException ioe){
            ioe.printStackTrace();
        }
        finally {
            sb.append("}");
            urlConnection.disconnect();
        }
        return sb.toString();
    }

    @Override
    public PhotoDto getById(Long id) {
        return photoMapper.photoToPhotoDto(photoRepository.findById(id));
    }

    public PhotoDto savePhoto(PhotoDto photoDto) {
        if(photoDto != null){
            Photo photo = photoMapper.photoDtoTophoto(photoDto);
            photoRepository.save(photo);
        }
        return null;
    }

    public PhotoDto savePhotos(List<PhotoDto> photosDto) {
        for (PhotoDto photoDto: photosDto) {
            if(photoDto != null){
                photoDto.setFileSize(getFileSize(photoDto.getUrl()));
                photoDto.setLocalPath(getLocalPath(photoDto.getId().toString(), photoDto.getUrl()));
                Photo photo = photoMapper.photoDtoTophoto(photoDto);
                photoRepository.save(photo);
            }
        }
        return null;
    }

    public String getFileSize(String link){
        double filesize = 0.0;
        String size = null;
        try {
            URL url = new URL(link);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            filesize = ((double) http.getContentLengthLong() / 1024);
            String finalSize = String.format("%.2f", filesize);
            size = String.valueOf(finalSize + "KB");
        }catch (MalformedURLException malformedURLException){
            System.out.println("->PhotoServiceIml::getFileSize-> MalformedURLException Caught");
            malformedURLException.printStackTrace();
        }
        catch (IOException ioe) {
            System.out.println("->PhotoServiceIml::getFileSize-> MalformedURLException Caught");
            ioe.printStackTrace();
        }
        catch (Exception e){
            System.out.println("->PhotoServiceIml::getFileSize-> Exception Caught");
        }
        return size;
    }

    public void downloadPhoto(String link, File out) throws IOException {
        BufferedOutputStream bout = null;
        BufferedInputStream in =null;

        try {
            URL url = new URL(link);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            double filesize = (double) http.getContentLengthLong();
            in = new BufferedInputStream(http.getInputStream());
            FileOutputStream fos = new FileOutputStream(out);
            bout = new BufferedOutputStream(fos, 1024);
            byte[] buffer = new byte[1024];
            double downloaded = 0.00;
            int read = 0;
            double precentDown;

            while ((read = in.read(buffer, 0, 1024)) >= 0) {
                bout.write(buffer, 0, read);
                downloaded += read;
                precentDown = (downloaded * 100) / filesize;
                String precent = String.format("%.2f", precentDown);
                System.out.println("Downloaded " + precent + "% of a file");
            }
            System.out.println("Download complete");

        }catch (IOException e) {
            e.printStackTrace();
        }
        catch (NullPointerException npe){
            npe.printStackTrace();
        }
        finally {
            bout.close();
            in.close();

        }
    }

    public void saveToDb(List<Photo> photos){
        for (Photo photo: photos) {
            savePhoto(photoMapper.photoToPhotoDto(java.util.Optional.ofNullable(photo)) );
        }
    }

    public List<Photo> mapPhotoObjects(JSONArray photoList) {
        List<Photo> rvPhotos = new ArrayList<>();

        for (int i = 0; i < photoList.length(); i++) {
            try {

                JSONObject jsonPhoto = photoList.getJSONObject(i);
                Photo photo = new Photo();

                photo.setId(Long.parseLong(jsonPhoto.get("id").toString(), 10));
                photo.setAlbumId(Long.parseLong(jsonPhoto.get("albumId").toString(), 10));
                photo.setTitle(jsonPhoto.get("title").toString());
                photo.setUrl(jsonPhoto.get("url").toString());
                photo.setThumbnailUrl(jsonPhoto.get("thumbnailUrl").toString());

                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                String fullPath = getLocalPath(photo.getId().toString(),photo.getUrl());

                photo.setCreatedDate(timestamp);
                photo.setLocalPath(fullPath);
                photo.setFileSize(getFileSize(photo.getUrl()));

                File out = new File(fullPath);
                downloadPhoto(photo.getUrl(), out);
                rvPhotos.add(photo);

            }catch (NumberFormatException nfe) {
                System.out.println("Not a valid ID exception Caught");
                nfe.printStackTrace();
            }catch (JSONException je){
                System.out.println("Json parse failed exception Caught");
                je.printStackTrace();
            }
            catch(NullPointerException npe) {
                System.out.print("NullPointerException Caught");
            }
            catch (Exception e ){
                e.printStackTrace();
            }
        }
        return rvPhotos;
    }

    public void downloadAfterInitial(PhotoDto dto) {

        List<Photo> rvPhotos = new ArrayList<>();
            try {
                Photo photo = new Photo();
                photo.setId(Long.parseLong(dto.getId().toString(), 10));
                photo.setAlbumId(Long.parseLong(dto.getAlbumId().toString(), 10));
                photo.setTitle(dto.getTitle());
                photo.setUrl(dto.getUrl());
                photo.setThumbnailUrl(dto.getThumbnailUrl());

                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                String fullPath = getLocalPath(dto.getId().toString(), dto.getUrl());

                photo.setCreatedDate(timestamp);
                photo.setLocalPath(fullPath);
                photo.setFileSize(getFileSize(dto.getUrl()));

                File out = new File(fullPath);
                downloadPhoto(photo.getUrl(), out);
                rvPhotos.add(photo);

            }catch (NumberFormatException nfe) {
                System.out.println("Not a valid ID exception Caught");
                nfe.printStackTrace();
            }
            catch(NullPointerException npe) {
                System.out.print("NullPointerException Caught");
            }
            catch (Exception e ){
                e.printStackTrace();
            }
    }


    public String getLocalPath(String id,String url){

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_CONST);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        sdf.setTimeZone(TimeZone.getDefault());
        String createdDate = sdf.format(timestamp);

        String ext = FilenameUtils.getExtension(url);
        String localPath = "C:\\photo\\";
        String fullPath = localPath + createdDate + "_" + getFileSize(url) + "_"+id + "." + ext;

        return fullPath;
    }
}
