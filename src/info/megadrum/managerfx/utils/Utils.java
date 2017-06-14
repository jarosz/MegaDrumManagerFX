package info.megadrum.managerfx.utils;

import info.megadrum.managerfx.data.Config3rd;
import info.megadrum.managerfx.data.ConfigConfigName;
import info.megadrum.managerfx.data.ConfigCurve;
import info.megadrum.managerfx.data.ConfigCustomName;
import info.megadrum.managerfx.data.ConfigGlobalMisc;
import info.megadrum.managerfx.data.ConfigMisc;
import info.megadrum.managerfx.data.ConfigPad;
import info.megadrum.managerfx.data.ConfigPedal;
import info.megadrum.managerfx.data.ConfigPositional;

public class Utils {

	public static void delayMs(int ms) {
	    try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			Utils.show_error("Unrecoverable timer error. Exiting.\n" +
					"(" + e.getMessage() + ")");
			System.exit(1);
		}
		
	}

	public static void show_error(String msg) {
		System.out.printf("Utils.show_error -> %s\n",msg);
/*		JOptionPane.showMessageDialog(null,
			    msg,
			    "Error",
			    JOptionPane.ERROR_MESSAGE);
*/
	}
	public static byte [] byte2sysex (byte b) {
		byte [] result = new byte[2];

		result[0] = (byte)((b&0xf0)>>4);
		result[1] = (byte)(b&0x0f);
		return result;
	}

	public static byte [] short2sysex (short s) {
		byte [] result = new byte[4];

		result[0] = (byte)((s&0x00f0)>>4);
		result[1] = (byte)(s&0x000f);
		result[2] = (byte)((s&0xf000)>>12);
		result[3] = (byte)((s&0x0f00)>>8);
		return result;
	}
	
	public static byte sysex2byte (byte [] sx) {
		byte result;
		
		result = (byte)(((sx[0]&0x0f)<<4)|(sx[1]&0x0f));
		return result;
	}

	public static short sysex2short (byte [] sx) {
		short result;
		
		result = (short)(((sx[0]&0x0f)<<4)|(sx[1]&0x0f)|((sx[2]&0x0f)<<12)|((sx[3]&0x0f)<<8));
		return result;
	}

	public static void copyConfigPadToSysex(ConfigPad config, byte [] sysex, int chainId, int padId) {
		byte [] sysex_byte = new byte[2];
		byte [] sysex_short = new byte[4];
		byte flags;
		int i = 0;

		sysex[i++] = Constants.SYSEX_START;
		sysex[i++] = Constants.MD_SYSEX;
		sysex[i++] = (byte)chainId; 
		sysex[i++] = Constants.MD_SYSEX_PAD;
		sysex[i++] = (byte)(padId + 1);
		
		if (config.inputDisabled) {
			sysex_byte = byte2sysex((byte)0);
		} else {
			sysex_byte = byte2sysex((byte)config.note);
		}
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = byte2sysex((byte)((config.channel<<4)|(config.curve)));
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = byte2sysex((byte)config.threshold);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = byte2sysex((byte)config.retrigger);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_short = short2sysex((short)config.levelMax);
		sysex[i++] = sysex_short[0];
		sysex[i++] = sysex_short[1];
		sysex[i++] = sysex_short[2];
		sysex[i++] = sysex_short[3];
		sysex_byte = byte2sysex((byte)config.minScan);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		flags = (byte) (((config.type)?1:0)|(((config.autoLevel)?1:0)<<1)|(((config.dual)?1:0)<<2)|(((config.threeWay)?1:0)<<3)
				|(config.gain<<4));
		sysex_byte = byte2sysex(flags);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = byte2sysex((byte)((config.xtalkGroup<<3)|(config.xtalkLevel)));
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = byte2sysex((byte)((config.dynLevel<<4)|(config.dynTime)));
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = byte2sysex((byte)(((config.function)<<6)|(config.shift<<3)|(config.compression)));
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];		
		sysex_byte = byte2sysex((byte)config.name);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = byte2sysex((byte)config.pressrollNote);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = byte2sysex((byte)config.altNote);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex[i++] = Constants.SYSEX_END;

	}

	public static void copySysexToConfigPad(byte [] sysex, ConfigPad config) {
		byte [] sysex_byte = new byte[2];
		byte [] sysex_short = new byte[4];
		byte flags;
		int i = 5;

		if (sysex.length >= Constants.MD_SYSEX_PAD_SIZE) {
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			if (!config.inputDisabled) {
				config.note = sysex2byte(sysex_byte);
			}
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			flags = sysex2byte(sysex_byte);
			config.curve = (flags&0x0f);
			config.channel = ((flags&0xf0)>>4);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			config.threshold = sysex2byte(sysex_byte);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			config.retrigger = sysex2byte(sysex_byte);
			if (config.retrigger <1 ) config.retrigger = 1;
			sysex_short[0] = sysex[i++];
			sysex_short[1] = sysex[i++];
			sysex_short[2] = sysex[i++];
			sysex_short[3] = sysex[i++];
			config.levelMax = sysex2short(sysex_short);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			config.minScan = sysex2byte(sysex_byte);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			flags = sysex2byte(sysex_byte);
			config.gain = ((flags&0xf0)>>4);
			config.type = ((flags&1) != 0);
			config.autoLevel = ((flags&(1<<1)) != 0);
			config.dual = ((flags&(1<<2)) != 0);
			config.threeWay = ((flags&(1<<3)) != 0);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			flags = sysex2byte(sysex_byte);
			config.xtalkGroup = ((flags&0x38)>>3);
			config.xtalkLevel = (flags&0x07);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			flags = sysex2byte(sysex_byte);
			config.dynTime = (flags&0x0f);
			config.dynLevel = ((flags&0xf0)>>4);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			flags = sysex2byte(sysex_byte);
			config.shift = ((flags&0x38)>>3);
			config.compression = (flags&0x07);
			config.function = ((flags&0xc0)>>6);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			config.name = sysex2byte(sysex_byte);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			config.pressrollNote = sysex2byte(sysex_byte);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			config.altNote = sysex2byte(sysex_byte);
		}
	}

	public static void copyConfigPosToSysex(ConfigPositional config, byte [] sysex, int chainId, int padId) {
		byte [] sysex_byte = new byte[2];
		int i = 0;

		sysex[i++] = Constants.SYSEX_START;
		sysex[i++] = Constants.MD_SYSEX;
		sysex[i++] = (byte)chainId; 
		sysex[i++] = Constants.MD_SYSEX_POS;
		sysex[i++] = (byte)(padId);
		
		sysex_byte = byte2sysex((byte)config.level);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = byte2sysex((byte)config.low);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = byte2sysex((byte)config.high);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex[i++] = Constants.SYSEX_END;

	}

	public static void copySysexToConfigPos(byte [] sysex, ConfigPositional config) {
		byte [] sysex_byte = new byte[2];
		int i = 5;

		if (sysex.length >= Constants.MD_SYSEX_POS_SIZE) {
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			config.level = sysex2byte(sysex_byte);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			config.low = sysex2byte(sysex_byte);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			config.high = sysex2byte(sysex_byte);
		}
	}

	public static void copyConfig3rdToSysex(Config3rd config, byte [] sysex, int chainId, int padId) {
		byte [] sysex_byte = new byte[2];
		int i = 0;

		sysex[i++] = Constants.SYSEX_START;
		sysex[i++] = Constants.MD_SYSEX;
		sysex[i++] = (byte)chainId;
		sysex[i++] = Constants.MD_SYSEX_3RD;
		sysex[i++] = (byte)padId;
		
		sysex_byte = byte2sysex((byte)config.note);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = byte2sysex((byte)config.threshold);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = byte2sysex((byte)config.pressrollNote);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = byte2sysex((byte)config.altNote);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = byte2sysex((byte)config.dampenedNote);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex[i++] = Constants.SYSEX_END;		
	}
	
	public static void copySysexToConfig3rd(byte [] sysex, Config3rd config) {
		byte [] sysex_byte = new byte[2];
		int i = 5;
		if (sysex.length >= Constants.MD_SYSEX_3RD_SIZE) {
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			config.note = sysex2byte(sysex_byte);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			config.threshold = (int)(sysex2byte(sysex_byte)&0xff);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			config.pressrollNote = sysex2byte(sysex_byte);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			config.altNote = sysex2byte(sysex_byte);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			config.dampenedNote = sysex2byte(sysex_byte);
		}
		
	}

	public static void copyConfigGlobalMiscToSysex(ConfigGlobalMisc config, byte [] sysex, int chainId) {
		byte [] sysex_byte = new byte[2];
		byte [] sysex_short = new byte[4];
		short flags;
		int i = 0;

		flags = (short) (
				(((config.custom_names_en)?1:0)<<1)|(((config.config_names_en)?1:0)<<2)|(((config.midi2_for_sysex)?1:0)<<5)
				);
		sysex[i++] = Constants.SYSEX_START;
		sysex[i++] = Constants.MD_SYSEX;
		sysex[i++] = (byte)chainId;
		sysex[i++] = Constants.MD_SYSEX_GLOBAL_MISC;
		
		sysex_byte = byte2sysex((byte)(100 - config.lcd_contrast));
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = byte2sysex((byte)config.inputs_count);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_short = short2sysex(flags);
		sysex[i++] = sysex_short[0];
		sysex[i++] = sysex_short[1];
		sysex[i++] = sysex_short[2];
		sysex[i++] = sysex_short[3];
		sysex[i++] = Constants.SYSEX_END;		
	}
	
	public static void copySysexToConfigGlobalMisc(byte [] sysex, ConfigGlobalMisc config) {
		byte [] sysex_byte = new byte[2];
		byte [] sysex_short = new byte[4];
		short flags;
		
		int i = 4;
		if (sysex.length >= Constants.MD_SYSEX_GLOBAL_MISC_SIZE) {
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			config.lcd_contrast = (100 - sysex2byte(sysex_byte));
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			config.inputs_count = sysex2byte(sysex_byte);
			sysex_short[0] = sysex[i++];
			sysex_short[1] = sysex[i++];
			sysex_short[2] = sysex[i++];
			sysex_short[3] = sysex[i++];
			flags = sysex2short(sysex_short);
			config.custom_names_en = ((flags&(1<<1)) != 0);
			config.config_names_en = ((flags&(1<<2)) != 0);
			config.midi2_for_sysex = ((flags&(1<<5)) != 0);
		}
		
	}
	
	public static void copySysexToConfigMisc(byte [] sysex, ConfigMisc config) {
		byte [] sysex_byte = new byte[2];
		byte [] sysex_short = new byte[4];
		short flags;
		int i = 4;
		//System.out.printf("sysex_byte size: %d\n", sysex_byte.length);
		//System.out.printf("sx size: %d\n", sx.length);
		if (sysex.length >= Constants.MD_SYSEX_MISC_SIZE) {
			sysex_short[0] = sysex[i++];
			sysex_short[1] = sysex[i++];
			sysex_short[2] = 0;
			sysex_short[3] = 0;
			config.setNoteOff(sysex2short(sysex_short));
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			config.latency = sysex2byte(sysex_byte);
			sysex_short[0] = sysex[i++];
			sysex_short[1] = sysex[i++];
			sysex_short[2] = sysex[i++];
			sysex_short[3] = sysex[i++];
			flags = sysex2short(sysex_short);
			sysex_short[0] = sysex[i++];
			sysex_short[1] = sysex[i++];
			sysex_short[2] = 0;
			sysex_short[3] = 0;
			config.pressroll = sysex2short(sysex_short);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			config.octave_shift = sysex2byte(sysex_byte);
			
			
			config.all_gains_low = ((flags&1) != 0);
			config.alt_note_choking = ((flags&(1<<1)) != 0);
			config.big_vu_meter = ((flags&(1<<2)) != 0);
			config.quick_access = ((flags&(1<<3)) != 0);
			config.big_vu_split = ((flags&(1<<4)) != 0);
			config.alt_false_tr_supp = ((flags&(1<<5)) != 0);
			config.inputs_priority = ((flags&(1<<6)) != 0);
			config.midi_thru = ((flags&(1<<8)) != 0);
			config.send_triggered_in = ((flags&(1<<11)) != 0);
		}
	}

	public static void copyConfigMiscToSysex(ConfigMisc config, byte [] sysex, int chainId) {
		byte [] sysex_byte = new byte[2];
		byte [] sysex_short = new byte[4];
		short flags;
		int i = 0;
		
		flags = (short) (((config.all_gains_low)?1:0)|
				(((config.alt_note_choking)?1:0)<<1)|(((config.big_vu_meter)?1:0)<<2)
				|(((config.quick_access)?1:0)<<3)|(((config.big_vu_split)?1:0)<<4)
				|(((config.alt_false_tr_supp)?1:0)<<5)|(((config.inputs_priority)?1:0)<<6)
				|(((config.midi_thru)?1:0)<<8)|(((config.send_triggered_in)?1:0)<<11));
		sysex[i++] = Constants.SYSEX_START;
		sysex[i++] = Constants.MD_SYSEX;
		sysex[i++] = (byte) chainId;
		sysex[i++] = Constants.MD_SYSEX_MISC;
		sysex_byte = byte2sysex((byte)config.getNoteOff());
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = byte2sysex((byte)config.latency);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_short = short2sysex(flags);
		sysex[i++] = sysex_short[0];
		sysex[i++] = sysex_short[1];
		sysex[i++] = sysex_short[2];
		sysex[i++] = sysex_short[3];
		sysex_byte = byte2sysex((byte)config.pressroll);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = byte2sysex((byte)config.octave_shift);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex[i++] = Constants.SYSEX_END;		
	}
	
	public static void copySysexToConfigPedal(byte [] sysex, ConfigPedal config, int mcu_type) {
		int sysex_length = Constants.MD_SYSEX_PEDAL_SIZE;
		if (mcu_type == Constants.MCU_TYPE_STM32F205TEST1) {
			sysex_length = Constants.MD_SYSEX_PEDAL_SIZE_OLD;
		}
		byte [] sysex_byte = new byte[2];
		byte [] sysex_short = new byte[4];
		byte flags;
		int i = 4;
		if (sysex.length >= sysex_length) {
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			flags = sysex2byte(sysex_byte);
			config.type = ((flags&1) != 0);
			config.autoLevels = ((flags&(1<<1)) != 0);
			config.altIn = ((flags&(1<<2)) != 0);
			config.reverseLevels = ((flags&(1<<3)) != 0);
			config.curve = ((flags&0xf0)>>4);
			
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			config.chickDelay = sysex2byte(sysex_byte);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			config.cc = sysex2byte(sysex_byte);
			sysex_short[0] = sysex[i++];
			sysex_short[1] = sysex[i++];
			sysex_short[2] = sysex[i++];
			sysex_short[3] = sysex[i++];
			config.lowLevel = sysex2short(sysex_short);
			sysex_short[0] = sysex[i++];
			sysex_short[1] = sysex[i++];
			sysex_short[2] = sysex[i++];
			sysex_short[3] = sysex[i++];
			config.highLevel = sysex2short(sysex_short);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			config.openLevel = sysex2byte(sysex_byte);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			config.closedLevel = sysex2byte(sysex_byte);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			config.shortThres = sysex2byte(sysex_byte);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			config.longThres = sysex2byte(sysex_byte);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			config.hhInput = sysex2byte(sysex_byte);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			flags = sysex2byte(sysex_byte);
			config.softChicks = ((flags&1) != 0);
			config.ccRdcLvl = ((flags&0x06)>>1);
			config.new_algorithm = (((flags&0x08)>>3) != 0);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			config.semiOpenLevel = sysex2byte(sysex_byte);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			config.halfOpenLevel = sysex2byte(sysex_byte);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			config.chickThres = sysex2byte(sysex_byte);
			sysex_short[0] = sysex[i++];
			sysex_short[1] = sysex[i++];
			sysex_short[2] = sysex[i++];
			sysex_short[3] = sysex[i++];
			config.chickParam1 = sysex2short(sysex_short);
			sysex_short[0] = sysex[i++];
			sysex_short[1] = sysex[i++];
			sysex_short[2] = sysex[i++];
			sysex_short[3] = sysex[i++];
			config.chickParam2 = sysex2short(sysex_short);
			sysex_short[0] = sysex[i++];
			sysex_short[1] = sysex[i++];
			sysex_short[2] = sysex[i++];
			sysex_short[3] = sysex[i++];
			config.chickParam3 = sysex2short(sysex_short);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			flags = sysex2byte(sysex_byte);
			config.chickCurve = (flags&0x0f);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			if (mcu_type != Constants.MCU_TYPE_STM32F205TEST1) {
				config.semiOpenLevel2 = sysex2byte(sysex_byte);
				sysex_byte[0] = sysex[i++];
				sysex_byte[1] = sysex[i++];
				config.halfOpenLevel2 = sysex2byte(sysex_byte);
				sysex_byte[0] = sysex[i++];
				sysex_byte[1] = sysex[i++];
			}
			config.bowSemiOpenNote = sysex2byte(sysex_byte);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			config.edgeSemiOpenNote = sysex2byte(sysex_byte);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			config.bellSemiOpenNote = sysex2byte(sysex_byte);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			if (mcu_type != Constants.MCU_TYPE_STM32F205TEST1) {
				config.bowSemiOpen2Note = sysex2byte(sysex_byte);
				sysex_byte[0] = sysex[i++];
				sysex_byte[1] = sysex[i++];
				config.edgeSemiOpen2Note = sysex2byte(sysex_byte);
				sysex_byte[0] = sysex[i++];
				sysex_byte[1] = sysex[i++];
				config.bellSemiOpen2Note = sysex2byte(sysex_byte);
				sysex_byte[0] = sysex[i++];
				sysex_byte[1] = sysex[i++];
			}
			config.bowHalfOpenNote = sysex2byte(sysex_byte);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			config.edgeHalfOpenNote = sysex2byte(sysex_byte);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			config.bellHalfOpenNote = sysex2byte(sysex_byte);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			if (mcu_type != Constants.MCU_TYPE_STM32F205TEST1) {
				config.bowHalfOpen2Note = sysex2byte(sysex_byte);
				sysex_byte[0] = sysex[i++];
				sysex_byte[1] = sysex[i++];
				config.edgeHalfOpen2Note = sysex2byte(sysex_byte);
				sysex_byte[0] = sysex[i++];
				sysex_byte[1] = sysex[i++];
				config.bellHalfOpen2Note = sysex2byte(sysex_byte);
				sysex_byte[0] = sysex[i++];
				sysex_byte[1] = sysex[i++];
			}
			config.bowSemiClosedNote = sysex2byte(sysex_byte);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			config.edgeSemiClosedNote = sysex2byte(sysex_byte);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			config.bellSemiClosedNote = sysex2byte(sysex_byte);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			config.bowClosedNote = sysex2byte(sysex_byte);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			config.edgeClosedNote = sysex2byte(sysex_byte);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			config.bellClosedNote = sysex2byte(sysex_byte);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			config.chickNote = sysex2byte(sysex_byte);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			config.splashNote = sysex2byte(sysex_byte);
		}
	}
	
	public static void copyConfigPedalToSysex(ConfigPedal config, byte [] sysex, int chainId, int mcu_type) {
		byte [] sysex_byte = new byte[2];
		byte [] sysex_short = new byte[4];
		byte flags;
		int i = 0;

		sysex[i++] = Constants.SYSEX_START;
		sysex[i++] = Constants.MD_SYSEX;
		sysex[i++] = (byte) chainId;
		sysex[i++] = Constants.MD_SYSEX_PEDAL;

		flags = (byte) (((config.type)?1:0)|(((config.autoLevels)?1:0)<<1)|(((config.altIn)?1:0)<<2)|(((config.reverseLevels)?1:0)<<3)
				|(config.curve<<4));
		sysex_byte = byte2sysex(flags);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = byte2sysex((byte)config.chickDelay);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = byte2sysex((byte)config.cc);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_short = short2sysex((short)config.lowLevel);
		sysex[i++] = sysex_short[0];
		sysex[i++] = sysex_short[1];
		sysex[i++] = sysex_short[2];
		sysex[i++] = sysex_short[3];
		sysex_short = short2sysex((short)config.highLevel);
		sysex[i++] = sysex_short[0];
		sysex[i++] = sysex_short[1];
		sysex[i++] = sysex_short[2];
		sysex[i++] = sysex_short[3];
		sysex_byte = byte2sysex((byte)config.openLevel);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = byte2sysex((byte)config.closedLevel);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = byte2sysex((byte)config.shortThres);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = byte2sysex((byte)config.longThres);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = byte2sysex((byte)config.hhInput);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		flags = (byte) ((config.softChicks)?1:0);
		flags = (byte) (flags|(config.ccRdcLvl<<1));
		flags = (byte) (flags|((config.new_algorithm?1:0)<<3));
		sysex_byte = byte2sysex((byte)flags);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = byte2sysex((byte)config.semiOpenLevel);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = byte2sysex((byte)config.halfOpenLevel);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = byte2sysex((byte)config.chickThres);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_short = short2sysex((short)config.chickParam1);
		sysex[i++] = sysex_short[0];
		sysex[i++] = sysex_short[1];
		sysex[i++] = sysex_short[2];
		sysex[i++] = sysex_short[3];
		sysex_short = short2sysex((short)config.chickParam2);
		sysex[i++] = sysex_short[0];
		sysex[i++] = sysex_short[1];
		sysex[i++] = sysex_short[2];
		sysex[i++] = sysex_short[3];
		sysex_short = short2sysex((short)config.chickParam3);
		sysex[i++] = sysex_short[0];
		sysex[i++] = sysex_short[1];
		sysex[i++] = sysex_short[2];
		sysex[i++] = sysex_short[3];
		flags = (byte) (config.chickCurve);
		sysex_byte = byte2sysex(flags);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		if (mcu_type != Constants.MCU_TYPE_STM32F205TEST1) {
			sysex_byte = byte2sysex((byte)config.semiOpenLevel2);
			sysex[i++] = sysex_byte[0];
			sysex[i++] = sysex_byte[1];
			sysex_byte = byte2sysex((byte)config.halfOpenLevel2);
			sysex[i++] = sysex_byte[0];
			sysex[i++] = sysex_byte[1];
		}
		sysex_byte = byte2sysex((byte)config.bowSemiOpenNote);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = byte2sysex((byte)config.edgeSemiOpenNote);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = byte2sysex((byte)config.bellSemiOpenNote);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		if (mcu_type != Constants.MCU_TYPE_STM32F205TEST1) {
			sysex_byte = byte2sysex((byte)config.bowSemiOpen2Note);
			sysex[i++] = sysex_byte[0];
			sysex[i++] = sysex_byte[1];
			sysex_byte = byte2sysex((byte)config.edgeSemiOpen2Note);
			sysex[i++] = sysex_byte[0];
			sysex[i++] = sysex_byte[1];
			sysex_byte = byte2sysex((byte)config.bellSemiOpen2Note);
			sysex[i++] = sysex_byte[0];
			sysex[i++] = sysex_byte[1];
		}
		sysex_byte = byte2sysex((byte)config.bowHalfOpenNote);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = byte2sysex((byte)config.edgeHalfOpenNote);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = byte2sysex((byte)config.bellHalfOpenNote);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		if (mcu_type != Constants.MCU_TYPE_STM32F205TEST1) {
			sysex_byte = byte2sysex((byte)config.bowHalfOpen2Note);
			sysex[i++] = sysex_byte[0];
			sysex[i++] = sysex_byte[1];
			sysex_byte = byte2sysex((byte)config.edgeHalfOpen2Note);
			sysex[i++] = sysex_byte[0];
			sysex[i++] = sysex_byte[1];
			sysex_byte = byte2sysex((byte)config.bellHalfOpen2Note);
			sysex[i++] = sysex_byte[0];
			sysex[i++] = sysex_byte[1];
		}
		sysex_byte = byte2sysex((byte)config.bowSemiClosedNote);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = byte2sysex((byte)config.edgeSemiClosedNote);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = byte2sysex((byte)config.bellSemiClosedNote);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = byte2sysex((byte)config.bowClosedNote);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = byte2sysex((byte)config.edgeClosedNote);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = byte2sysex((byte)config.bellClosedNote);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = byte2sysex((byte)config.chickNote);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = byte2sysex((byte)config.splashNote);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex[i++] = Constants.SYSEX_END;
	}

	public static int validateInt(int value, int min, int max, int fallBack){
		//returns value if min <= value <= max
		//otherwise returns fallBack
		if ((value>=min) && (value<=max)) {
			return value;
		} else {
			return fallBack;
		}
	}
	
	public static Double validateDouble(Double value, Double min, Double max, Double fallBack){
		//returns value if min <= value <= max
		//otherwise returns fallBack
		if ((value>=min) && (value<=max)) {
			return value;
		} else {
			return fallBack;
		}
	}
	
	public static short validateShort(short value, int min, int max, short fallBack){
		//returns value if min <= value <= max
		//otherwise returns fallBack
		if ((value>=min) && (value<=max)) {
			return value;
		} else {
			return fallBack;
		}
	}

}
