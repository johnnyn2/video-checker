package com.video.checker;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jcodec.containers.mp4.boxes.MetaValue;
import org.jcodec.movtool.MetadataEditor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import it.sauronsoftware.jave.AudioInfo;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.MultimediaInfo;
import it.sauronsoftware.jave.VideoInfo;

@RestController
public class ApiController {

    @PostMapping(value = "/uploadJAVE", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, Object> upload(@RequestParam(required = true, value="uploadVideo") MultipartFile uploadVideo) {
        Map<String, Object> data = new HashMap<>();
        File source = new File("C:/Users/johnnyho/Documents/video-checker/uploadVideos.mp4");
        try {
			uploadVideo.transferTo(source);
		} catch (IllegalStateException e1) {
            // e1.printStackTrace();
            data.put("IllegalStateException", "true");
            
		} catch (IOException e1) {
            // e1.printStackTrace();
            data.put("IOException_file_transfer", "true");
		}
        Encoder encoder = new Encoder();
        try {
            MultimediaInfo info = encoder.getInfo(source);
            VideoInfo vInfo = info.getVideo();
            Map<String, Object> vInfoData = new HashMap<>();
            vInfoData.put("bitRate", vInfo.getBitRate());
            vInfoData.put("frameRate", vInfo.getFrameRate());
            vInfoData.put("size", vInfo.getSize());
            vInfoData.put("duration", info.getDuration());
            vInfoData.put("format", info.getFormat());
            data.put("videoInfo", vInfoData);

            AudioInfo aInfo = info.getAudio();
            Map<String, Object> aInfoData = new HashMap<>();
            aInfoData.put("bitRate", aInfo.getBitRate());
            aInfoData.put("channels", aInfo.getChannels());
            aInfoData.put("samplingRate", aInfo.getSamplingRate());

            data.put("audioInfo", aInfoData);
        } catch(EncoderException e) {
            // e.printStackTrace();
            data.put("IOException_file_encoder", "true");
        } catch (NullPointerException ne) {
            data.put("NullPointerException_file_encoder", "true");
        }
        return data;
    }

    @PostMapping(value = "/uploadJCodec", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, Object> uploadJCodec(@RequestParam(required = true, value="uploadVideo") MultipartFile uploadVideo) {
        Map<String, Object> data = new HashMap<>();
        File source = new File("C:/Users/johnnyho/Documents/video-checker/uploadVideos.mp4");
        try {
			uploadVideo.transferTo(source);
		} catch (IllegalStateException e1) {
            // e1.printStackTrace();
            data.put("IllegalStateException", "true");
            
		} catch (IOException e1) {
            // e1.printStackTrace();
            data.put("IOException_file_transfer", "true");
		}
        try {
            MetadataEditor mediaMeta = MetadataEditor.createFrom(source);
            Map<String, MetaValue> keyedMeta = mediaMeta.getKeyedMeta();
            data.put("keyedMeta", keyedMeta);
            Map<Integer, MetaValue> itunesMeta = mediaMeta.getItunesMeta();
            data.put("itunesMeta", itunesMeta);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return data;
    }
}
