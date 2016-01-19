package info.pinlab.snd.fe;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.junit.BeforeClass;
import org.junit.Test;
import org.lwjgl.Sys;

import info.pinlab.snd.fe.ParamSheet.ParamSheetBuilder;
import info.pinlab.snd.fe.Windower.WindowType;

public class HanningWindower320Test {

	static ParamSheet context;
	static double error = 0.00001;
	static double [] exp;
	static double [] intSampArr;
	static int size;
	static int frameLen = 20;
	static int hz = 16000;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		context = new ParamSheetBuilder()
				.set(FEParam.FRAME_PROCESSORS, "windowning")
				.setFrameLenInMs(frameLen)
				.setWinType(WindowType.HANNING)
				.build();
		
		size = context.get(FEParam.FRAME_LEN_SAMPLE);
		System.err.println("size: " + size);
		
		// Read the int arrary of wa file (test.wav) for test
		exp = new double[size];
		intSampArr = new double[size];
		
		try{
			System.out.println(HanningWindower320Test.class.getResource("test_int_320_wav.txt").getPath());
			FileReader fr = new FileReader(HanningWindower320Test.class.getResource("test_int_320_wav.txt").getPath());
			BufferedReader br = new BufferedReader(fr);
			int ix = 0;
			String line;
			while((line = br.readLine()) !=null){
				intSampArr[ix] = Double.parseDouble(line);
				ix++;
			}
			br.close();
			}catch(FileNotFoundException e){
				System.err.println(e);
			}

		// Read the result of hanning windowning by python .
		try{
			System.err.println(HanningWindower320Test.class.getResource("TestHanningWindower320.txt").getPath());
			FileReader fr = new FileReader(HanningWindower320Test.class.getResource("TestHanningWindower320.txt").getPath());
			BufferedReader br = new BufferedReader(fr);
			int ix = 0;
			String line;
			while((line = br.readLine())!=null){
//				System.err.println("testRes: " + line + " ix: " + ix);
				exp[ix] = Double.parseDouble(line);
				ix++;
			}
			br.close();
		}catch(FileNotFoundException e){
			System.err.println(e);
		}
	}


	@Test
	public void testProcess() throws Exception {
		HanningWindower hannWindow = new HanningWindower(context);
		double [] obs = new double [size];
		obs = hannWindow.process(intSampArr);
		
		for(int i = 0; i<size; i++){
			System.err.print("res: " + obs[i] + " expected: " + exp[i]);
		}

		assertArrayEquals(exp, obs, error);
	}

}
