package com.shengliedu.parent.util;

import java.io.File;
import java.io.IOException;

import android.media.AudioFormat;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

public class AudioRecorder {
	private static int SAMPLE_RATE_IN_HZ = 44100;

	final MediaRecorder recorder = new MediaRecorder();
	final String path;

	public AudioRecorder(String path) {
		this.path = sanitizePath(path);
	}

	private String sanitizePath(String path) {
		if (!path.startsWith("/")) {
			path = "/" + path;
		}
		if (!path.contains(".")) {
			path += ".mp3";
		}
		return Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/zzkt" + path;
	}

	public void start() throws IOException {
		String state = Environment.getExternalStorageState();
		if (!state.equals(Environment.MEDIA_MOUNTED)) {
			throw new IOException("SD Card is not mounted,It is  " + state
					+ ".");
		}
		File directory = new File(path).getParentFile();
		if (!directory.exists() && !directory.mkdirs()) {
			throw new IOException("Path to file could not be created");
		}
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
		recorder.setAudioChannels(AudioFormat.CHANNEL_CONFIGURATION_MONO);
		recorder.setAudioSamplingRate(SAMPLE_RATE_IN_HZ);
		recorder.setOutputFile(path);
		recorder.prepare();
		recorder.start();
	}

	public void stop() throws IOException {
		Log.v("TAG", recorder + "");
		recorder.stop();
		recorder.release();
	}

	public double getAmplitude() {
		if (recorder != null) {
			return (recorder.getMaxAmplitude());
		} else
			return 0;
	}
}