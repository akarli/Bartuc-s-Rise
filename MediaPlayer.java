import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;


public class MediaPlayer implements Runnable{

	private File filename;

    private AudioInputStream audioStream = null;
    private AudioFormat audioFormat = null;
    private SourceDataLine sourceLine = null;
    
    private volatile boolean stopFlag = false;
    private volatile boolean pauseFlag = false;
    private volatile boolean isPlayingFlag = false;
    private volatile float volume_dB = 0.0f;

    public MediaPlayer()
    {
    }
    
    public MediaPlayer(File file)
    {
    	filename = file;
    }
    
    /**
     * @param filename the name of the file that is going to be played
     * @throws IOException 
     * @throws UnsupportedAudioFileException 
     * @throws FileNotFoundException 
     * @throws LineUnavailableException 
     */
    public void playSound() throws FileNotFoundException, UnsupportedAudioFileException, IOException, LineUnavailableException
    {
    	isPlayingFlag = true;
    	
        if (filename.getName().toLowerCase().endsWith(".txt"))
        {
        	System.out.println("Text Files Not Supported!");
        }
        else 
        {
        	
            final AudioInputStream in = AudioSystem.getAudioInputStream(filename);

            final AudioFormat baseFormat = in.getFormat();
            audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                    baseFormat.getSampleRate(),
                    16,
                    baseFormat.getChannels(),
                    baseFormat.getChannels() * 2,
                    baseFormat.getSampleRate(),
                    false);               
            audioStream = AudioSystem.getAudioInputStream(audioFormat, in);        
            final byte[] data = new byte[4096];  
            try{
                SourceDataLine res = null;
                DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
                res = (SourceDataLine) AudioSystem.getLine(info);
                res.open(audioFormat);
                sourceLine = res;
                
                // Start
                onPlay();
                sourceLine.start();
                int nBytesRead = 0;// nBytesWritten = 0;
                while ((nBytesRead != -1) && (!stopFlag))
                {
                	if(!pauseFlag)
                	{
                		isPlayingFlag = true;
	                    nBytesRead = audioStream.read(data, 0, data.length);
	                    if (nBytesRead != -1)
	                    {
	                    	if (sourceLine.isControlSupported(FloatControl.Type.MASTER_GAIN)) 
	                    	{
	                            ((FloatControl) sourceLine.getControl(FloatControl.Type.MASTER_GAIN)).setValue(volume_dB);
	                        }
	                    	sourceLine.write(data, 0, nBytesRead);
	                    }
                	}
                	else
                	{
                		isPlayingFlag = false;
                	}
                }         
                
                // Stop
                sourceLine.drain();
                sourceLine.stop();
                sourceLine.close();
                audioStream.close();
            }catch(LineUnavailableException e){
                e.printStackTrace();
            }
            in.close();
            
            isPlayingFlag = false;
            onStop();
        	 	
        }
    }

	@Override
	public void run() 
	{
		stopFlag = false;
		pauseFlag = false;

		try {
			playSound();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	public void play() 
	{
		run();
	}

	public void onPlay(){}

	public void pause() 
	{
		pauseFlag = true;
		onPause();
	}

	public void onPause(){}

	public void resume() 
	{
		pauseFlag = false;
		onResume();
	}

	public void onResume(){}

	public void stop() 
	{
		stopFlag = true;
	}

	public void onStop(){}

	public boolean isPlaying()
	{
		return isPlayingFlag;
	}

	public boolean isPaused()
	{
		return pauseFlag;
	}

	public void setFile(File filename)
    {
    	this.filename = filename;
    }

	public void setVolume(Float volume)
	{
		volume = volume==null ? 1.0F : volume;
		volume = volume<=0.0F ? 0.0001F : volume;
		setVolumeInDecibels((float)( 20.0*(Math.log(volume)/Math.log(10.0)) ));
	}

	public void setVolumeInDecibels(Float decibels)
	{
		decibels = decibels==null ? 0.0F : decibels;
		this.volume_dB = decibels;
	}
}
