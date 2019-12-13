package tune;

import java.io.*;
import java.lang.Math;
import java.util.*;
import javax.sound.midi.*;

/**
 * A little example showing how to play a tune in Java.
 * 
 * Inputs are not sanitized or checked, this is just to show how simple it is.
 * 
 * @author Peter
 */
public class Synth {

	private static List<String> notes = Arrays.asList("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B");
	private static List<String> reg = Arrays.asList("C", "D", "E", "F", "G", "A", "B");
	private static MidiChannel[] channels;
	private static int INSTRUMENT = 0; // 0 is a piano, 9 is percussion, other channels are for other instruments
	private static int VOLUME = 50; // between 0 et 127

	public static void main(String[] args) {

		try {
			// * Open a synthesizer
			Synthesizer synth = MidiSystem.getSynthesizer();
			synth.open();
			channels = synth.getChannels();

			int seqArr[] = new int[50];
			int rhythm[] = new int[50];
			int tone[] = new int[50];

			long f = 2;
			long f2 = 6;
			for (int i = 0; i < 47; i += 2) {
				f = f + f2;
				f2 = f - f2;
				seqArr[i] = (int) (f2 % 7);
				seqArr[i + 1] = (int) (f2 % 5);
				 //seqArr[i+2] = (int)(f2%6);
				// seqArr[i+3] = (int)(f2%5);
				rhythm[i] = (int) ((Math.random() * 3)) - 1;
				//tone[i] = (int) ((Math.random() * 3)) - 1;
				tone[i] = (int) (f%3)-1;
				tone[i+1] = tone[i];
				
				
				
			}

			int j = 0;
			// * Play some notes
			for (int i = 1; i < seqArr.length - 4; i++) {
				// rest(100);
				play((3 + tone[i]) + reg.get(seqArr[i+1]), 300);
				play((3 + tone[i]) + reg.get(seqArr[i]), 300);
				play((3 + tone[i]) + reg.get(seqArr[i + 2]), 300);
				play((3 + tone[i]) + reg.get(seqArr[i + 3]), 300);

			}
			rest(500);

			// * finish up
			synth.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Plays the given note for the given duration
	 */
	private static void play(String note, int duration) throws InterruptedException {
		// * start playing a note
		channels[INSTRUMENT].noteOn(id(note), VOLUME);
		// * wait
		Thread.sleep(duration);
		// * stop playing a note
		channels[INSTRUMENT].noteOff(id(note));
	}

	/**
	 * Plays nothing for the given duration
	 */
	private static void rest(int duration) throws InterruptedException {
		Thread.sleep(duration);
	}

	/**
	 * Returns the MIDI id for a given note: eg. 4C -> 60
	 * 
	 * @return
	 */
	private static int id(String note) {
		int octave = Integer.parseInt(note.substring(0, 1));
		return notes.indexOf(note.substring(1)) + 12 * octave + 12;
	}
}