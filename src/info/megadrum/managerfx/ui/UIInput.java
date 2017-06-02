package info.megadrum.managerfx.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.event.EventListenerList;

import info.megadrum.managerfx.data.ConfigMisc;
import info.megadrum.managerfx.data.ConfigPad;
import info.megadrum.managerfx.data.ConfigPositional;
import info.megadrum.managerfx.utils.Constants;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Separator;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class UIInput {
//	private VBox layout;
	private TitledPane		titledPane;
	
	private UIComboBox 		uiComboBoxName;
	private UISpinnerNote 	uiSpinnerNoteMainNote;
	private UISpinnerNote 	uiSpinnerNoteAltNote;
	private UISpinnerNote 	uiSpinnerNotePressNote;
	private UISpinner 		uiSpinnerChannel;
	private UIComboBox 		uiComboBoxFunction;
	private UIComboBox 		uiComboBoxCurve;
	private UIComboBox 		uiComboBoxCompression;
	private UIComboBox 		uiComboBoxLevelShift;
	private UIComboBox 		uiComboBoxXTalkLevel;
	private UIComboBox 		uiComboBoxXTalkGroup;
	private UISpinner 		uiSpinnerThreshold;
	private UIComboBox 		uiComboBoxGain;
	private UICheckBox 		uiCheckBoxHighAuto;
	private UISpinner 		uiSpinnerHighLevel;
	private UISpinner 		uiSpinnerRetrigger;
	private UIComboBox 		uiComboBoxDynLevel;
	private UIComboBox 		uiComboBoxDynTime;
	private UISpinner 		uiSpinnerMinScan;
	private UIComboBox 		uiComboBoxPosLevel;
	private UISpinner 		uiSpinnerPosLow;
	private UISpinner 		uiSpinnerPosHigh;
	private UIComboBox 		uiComboBoxType;
	
	private List<String>    listInputName;
	private List<String>    listInputType;
	private int				inputType;
	
	private Boolean			copyPressed = false;
	private int				copyPressedValueId = -1;
	
	//private ConfigPad			configPad;
	//private ConfigPositional	configPos;
		
	private ArrayList<UIControl> allControls;
	
	protected EventListenerList listenerList = new EventListenerList();
	
	public void addControlChangeEventListener(ControlChangeEventListener listener) {
		listenerList.add(ControlChangeEventListener.class, listener);
	}
	public void removeControlChangeEventListener(ControlChangeEventListener listener) {
		listenerList.remove(ControlChangeEventListener.class, listener);
	}
	protected void fireControlChangeEvent(ControlChangeEvent evt, Integer parameter) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = 0; i < listeners.length; i = i+2) {
			if (listeners[i] == ControlChangeEventListener.class) {
				((ControlChangeEventListener) listeners[i+1]).controlChangeEventOccurred(evt, parameter);
			}
		}
	}
	
	public UIInput(String title) {
		allControls = new ArrayList<UIControl>();

		VBox layout = new VBox();

		uiComboBoxName = new UIComboBox("Name", true);
		allControls.add(uiComboBoxName);

		uiSpinnerNoteMainNote = new UISpinnerNote("Note", true);
		uiSpinnerNoteMainNote.setNoteIsMain(true);
		uiSpinnerNoteMainNote.setDisabledNoteAllowed(true);
		allControls.add(uiSpinnerNoteMainNote);
		
		uiSpinnerNoteAltNote = new UISpinnerNote("AltNote", true, true);
		allControls.add(uiSpinnerNoteAltNote);

		uiSpinnerNotePressNote = new UISpinnerNote("PressrollNote", true, true);
		allControls.add(uiSpinnerNotePressNote);

		uiSpinnerChannel = new UISpinner("Channel", 1, 16, 10, 1, true);
		allControls.add(uiSpinnerChannel);
		
		uiComboBoxFunction = new UIComboBox("Function", true);
		uiComboBoxFunction.uiCtlSetValuesArray(Arrays.asList(Constants.PAD_FUNCTION_LIST));
		allControls.add(uiComboBoxFunction);

		uiComboBoxCurve = new UIComboBox("Curve", true);
		uiComboBoxCurve.uiCtlSetValuesArray(Arrays.asList(Constants.CURVES_LIST));
		allControls.add(uiComboBoxCurve);

		uiComboBoxCompression = new UIComboBox("Compression", true);
		uiComboBoxCompression.uiCtlSetValuesArray(Arrays.asList(Constants.PAD_COMPRESSION_LIST));
		allControls.add(uiComboBoxCompression);

		uiComboBoxLevelShift = new UIComboBox("Level Shift", true);
		uiComboBoxLevelShift.uiCtlSetValuesArray(Arrays.asList(Constants.PAD_LEVEL_SHIFT_LIST));
		allControls.add(uiComboBoxLevelShift);

		uiComboBoxXTalkLevel = new UIComboBox("XTalk Level", true);
		uiComboBoxXTalkLevel.uiCtlSetValuesArray(Arrays.asList(Constants.PAD_XTALK_LEVEL_LIST));
		allControls.add(uiComboBoxXTalkLevel);

		uiComboBoxXTalkGroup = new UIComboBox("XTalk Group", true);
		uiComboBoxXTalkGroup.uiCtlSetValuesArray(Arrays.asList(Constants.PAD_XTALK_GROUP_LIST));
		allControls.add(uiComboBoxXTalkGroup);

		uiSpinnerThreshold = new UISpinner("Threshold", 0, 127, 30, 1, true);
		allControls.add(uiSpinnerThreshold);

		uiComboBoxGain = new UIComboBox("Gain", true);
		uiComboBoxGain.uiCtlSetValuesArray(Arrays.asList(Constants.PAD_GAIN_LIST));
		allControls.add(uiComboBoxGain);

		uiCheckBoxHighAuto = new UICheckBox("HighLevelAuto", true);
		allControls.add(uiCheckBoxHighAuto);
		
		uiSpinnerHighLevel = new UISpinner("HighLevel", 64, 1023, 64, 1, true);
		allControls.add(uiSpinnerHighLevel);

		uiSpinnerRetrigger = new UISpinner("Retrigger Mask", 0, 127, 8, 1, true);
		allControls.add(uiSpinnerRetrigger);

		uiComboBoxDynLevel = new UIComboBox("DynLevel", true);
		uiComboBoxDynLevel.uiCtlSetValuesArray(Arrays.asList(Constants.PAD_DYN_LEVEL_LIST));
		allControls.add(uiComboBoxDynLevel);

		uiComboBoxDynTime = new UIComboBox("DynTime", true);
		uiComboBoxDynTime.uiCtlSetValuesArray(Arrays.asList(Constants.PAD_DYN_TIME_LIST));
		allControls.add(uiComboBoxDynTime);

		uiSpinnerMinScan = new UISpinner("MinScan", 10, 100, 20, 1, true);
		allControls.add(uiSpinnerMinScan);

		uiComboBoxPosLevel = new UIComboBox("Pos Level", true);
		uiComboBoxPosLevel.uiCtlSetValuesArray(Arrays.asList(Constants.PAD_POS_LEVEL_LIST));
		allControls.add(uiComboBoxPosLevel);

		uiSpinnerPosLow = new UISpinner("Pos Low", 0, 100, 5, 1, true);
		allControls.add(uiSpinnerPosLow);

		uiSpinnerPosHigh = new UISpinner("Pos High", 0, 100, 15, 1, true);
		allControls.add(uiSpinnerPosHigh);
		
		uiComboBoxType = new UIComboBox("Type", true);
		setHeadEdgeType(Constants.PAD_TYPE_HEAD);
		allControls.add(uiComboBoxType);
	
		for (int i = 0; i < allControls.size(); i++) {
			final int iFinal = i;
        	layout.getChildren().add(allControls.get(i).getUI());
        	allControls.get(i).setValueId(i + Constants.INPUT_VALUE_ID_MIN);
        	allControls.get(i).setLabelWidthMultiplier(Constants.FX_INPUT_LABEL_WIDTH_MUL);        	
        	allControls.get(i).addControlChangeEventListener(new ControlChangeEventListener() {
				
				@Override
				public void controlChangeEventOccurred(ControlChangeEvent evt, Integer parameter) {
					// TODO Auto-generated method stub
					if (allControls.get(iFinal).isCopyPressed()) {
						allControls.get(iFinal).resetCopyPressed();
						copyPressedValueId = allControls.get(iFinal).getValueId();
						copyPressed = true;
						fireControlChangeEvent(new ControlChangeEvent(this), parameter);
					} else {
						if (parameter == Constants.CONTROL_CHANGE_EVENT_NOTE_LINKED) {
							linkedNoteStateChanged();
						} else {
							if (parameter == Constants.CONTROL_CHANGE_EVENT_NOTE_MAIN) {
								linkedNoteStateChanged();
							}
							fireControlChangeEvent(new ControlChangeEvent(this), parameter);
						}
					}
				}
			});
       }

		titledPane = new TitledPane();
		titledPane.setText(title);
		titledPane.setContent(layout);
		titledPane.setCollapsible(false);
		
		setAllStateUnknown();
	}

	public int getCopyPressedValueId() {
		return copyPressedValueId;
	}
	
	public Boolean isCopyPressed() {
		return copyPressed;
	}
	
	public void resetCopyPressed() {
		copyPressed = false;
	}

	public void setAllStateUnknown() {
		for (int i = 0; i < allControls.size(); i++ ) {
			allControls.get(i).setSyncState(Constants.SYNC_STATE_UNKNOWN);
		}
	}
	
	public void setAllStateNotSynced() {
		for (int i = 0; i < allControls.size(); i++ ) {
			allControls.get(i).setSyncState(Constants.SYNC_STATE_NOT_SYNCED);
		}
	}

	public Node getUI() {
		return (Node) titledPane;
	}

	public void respondToResize(Double h, Double w, Double fullHeight, Double controlH, Double controlW) {
		Double titledPaneFontHeight = fullHeight*Constants.FX_SUB_TITLEBARS_FONT_SCALE;
		if (titledPaneFontHeight > Constants.FX_TITLEBARS_FONT_MIN_SIZE) {
			titledPane.setFont(new Font(titledPaneFontHeight));
		} else {
			titledPane.setFont(new Font(Constants.FX_TITLEBARS_FONT_MIN_SIZE));
		}
		//System.out.println("Responding to scene resize in UIMisc");
		for (int i = 0; i < allControls.size(); i++) {
			allControls.get(i).respondToResize(controlH, controlW*Constants.FX_INPUT_CONTROL_WIDTH_MUL);
        }
		//titledPane.setMinHeight(h);
		//titledPane.setMaxHeight(h);

	}

	public void setHeadEdgeType(int type) {
		inputType = type;
		if (type == Constants.PAD_TYPE_HEAD) {
			listInputType = Arrays.asList(Constants.PAD_TYPE_HEAD_LIST);
			for (int i = 0; i < allControls.size(); i++) {
				allControls.get(i).setButtonCopyToolTip("Copy this setting to All inputs");
	        }
		}
		if (type == Constants.PAD_TYPE_EDGE) {
			listInputType = Arrays.asList(Constants.PAD_TYPE_EDGE_LIST);
			for (int i = 0; i < allControls.size(); i++) {
				allControls.get(i).setButtonCopyToolTip("Copy head/rim setting to All pads");
	        }
		}
		uiComboBoxType.uiCtlSetValuesArray(listInputType);
	}
	
	public void setNameList(List<String> list) {
		uiComboBoxName.uiCtlSetValuesArray(list);
	}

	public void setMdValuesFromConfigPos(ConfigPositional configPos) {
		uiComboBoxPosLevel.uiCtlSetMdValue(configPos.level);
		uiSpinnerPosLow.uiCtlSetMdValue(configPos.low);
		uiSpinnerPosHigh.uiCtlSetMdValue(configPos.high);
	}
	
	public void setMdValuesFromConfigPad(ConfigPad configPad) {
		uiComboBoxName.uiCtlSetMdValue(configPad.name);
		uiSpinnerNoteMainNote.uiCtlSetMdValue(configPad.note);
		uiSpinnerNoteAltNote.uiCtlSetMdValue(configPad.altNote);
		uiSpinnerNotePressNote.uiCtlSetMdValue(configPad.pressrollNote);
		uiSpinnerChannel.uiCtlSetMdValue(configPad.channel + 1);
		uiComboBoxFunction.uiCtlSetMdValue(configPad.function);
		uiComboBoxCurve.uiCtlSetMdValue(configPad.curve);
		uiComboBoxCompression.uiCtlSetMdValue(configPad.compression);
		uiComboBoxLevelShift.uiCtlSetMdValue(configPad.shift);
		uiComboBoxXTalkLevel.uiCtlSetMdValue(configPad.xtalkLevel);
		uiComboBoxXTalkGroup.uiCtlSetMdValue(configPad.xtalkGroup);
		uiSpinnerThreshold.uiCtlSetMdValue(configPad.threshold);
		uiComboBoxGain.uiCtlSetMdValue(configPad.gain);
		uiCheckBoxHighAuto.uiCtlSetMdValue(configPad.autoLevel);
		uiSpinnerHighLevel.uiCtlSetMdValue(configPad.levelMax);
		uiSpinnerRetrigger.uiCtlSetMdValue(configPad.retrigger);
		uiComboBoxDynLevel.uiCtlSetMdValue(configPad.dynLevel);
		uiComboBoxDynTime.uiCtlSetMdValue(configPad.dynTime);
		uiSpinnerMinScan.uiCtlSetMdValue(configPad.minScan);
		uiComboBoxType.uiCtlSetMdValue(configPad.getTypeInt());
	}
	
	public void setControlsFromConfigPos(ConfigPositional configPos, Boolean setFromSysex) {
		uiComboBoxPosLevel.uiCtlSetValue(configPos.level, setFromSysex);
		uiSpinnerPosLow.uiCtlSetValue(configPos.low, setFromSysex);
		uiSpinnerPosHigh.uiCtlSetValue(configPos.high, setFromSysex);
	}
	
	private void linkedNoteStateChanged() {
		if (uiSpinnerNoteAltNote.getLinked()) {
			uiSpinnerNoteAltNote.uiCtlSetValue(uiSpinnerNoteMainNote.uiCtlGetValue(), false);
		}
		uiSpinnerNoteAltNote.setLinked(uiSpinnerNoteAltNote.getLinked());
		if (uiSpinnerNotePressNote.getLinked()) {
			uiSpinnerNotePressNote.uiCtlSetValue(uiSpinnerNoteMainNote.uiCtlGetValue(), false);
		}
		uiSpinnerNotePressNote.setLinked(uiSpinnerNotePressNote.getLinked());
	}
	
	public void setControlsFromConfigPad(ConfigPad configPad, Boolean setFromSysex) {
		uiComboBoxName.uiCtlSetValue(configPad.name, setFromSysex);
		uiSpinnerNoteMainNote.uiCtlSetValue(configPad.note, setFromSysex);
		if (configPad.altNote_linked) {
			uiSpinnerNoteAltNote.uiCtlSetValue(configPad.note, setFromSysex);
		} else {
			uiSpinnerNoteAltNote.uiCtlSetValue(configPad.altNote, setFromSysex);			
		}
		uiSpinnerNoteAltNote.setLinked(configPad.altNote_linked);		
		if (configPad.pressrollNote_linked) {
			uiSpinnerNotePressNote.uiCtlSetValue(configPad.note, setFromSysex);
		} else {
			uiSpinnerNotePressNote.uiCtlSetValue(configPad.pressrollNote, setFromSysex);			
		}
		uiSpinnerNotePressNote.setLinked(configPad.pressrollNote_linked);
		uiSpinnerNotePressNote.uiCtlSetValue(configPad.pressrollNote, setFromSysex);
		uiSpinnerChannel.uiCtlSetValue(configPad.channel + 1, setFromSysex);
		uiComboBoxFunction.uiCtlSetValue(configPad.function, setFromSysex);
		uiComboBoxCurve.uiCtlSetValue(configPad.curve, setFromSysex);
		uiComboBoxCompression.uiCtlSetValue(configPad.compression, setFromSysex);
		uiComboBoxLevelShift.uiCtlSetValue(configPad.shift, setFromSysex);
		uiComboBoxXTalkLevel.uiCtlSetValue(configPad.xtalkLevel, setFromSysex);
		uiComboBoxXTalkGroup.uiCtlSetValue(configPad.xtalkGroup, setFromSysex);
		uiSpinnerThreshold.uiCtlSetValue(configPad.threshold, setFromSysex);
		uiComboBoxGain.uiCtlSetValue(configPad.gain, setFromSysex);
		uiCheckBoxHighAuto.uiCtlSetValue(configPad.autoLevel, setFromSysex);
		uiSpinnerHighLevel.uiCtlSetValue(configPad.levelMax, setFromSysex);
		uiSpinnerRetrigger.uiCtlSetValue(configPad.retrigger, setFromSysex);
		uiComboBoxDynLevel.uiCtlSetValue(configPad.dynLevel, setFromSysex);
		uiComboBoxDynTime.uiCtlSetValue(configPad.dynTime, setFromSysex);
		uiSpinnerMinScan.uiCtlSetValue(configPad.minScan, setFromSysex);
		uiComboBoxType.uiCtlSetValue(configPad.getTypeInt(), setFromSysex);
	}
	
	public void setConfigPosFromControls(ConfigPositional configPos) {
		configPos.level = uiComboBoxPosLevel.uiCtlGetValue();
		configPos.low = uiSpinnerPosLow.uiCtlGetValue();
		configPos.high = uiSpinnerPosHigh.uiCtlGetValue();		
	}
	
	public void setConfigPadFromControls(ConfigPad configPad) {
		configPad.name = uiComboBoxName.uiCtlGetValue();
		configPad.note = uiSpinnerNoteMainNote.uiCtlGetValue();
		if (uiSpinnerNoteAltNote.getLinked()) {
			configPad.altNote = uiSpinnerNoteMainNote.uiCtlGetValue();
		} else {
			configPad.altNote = uiSpinnerNoteAltNote.uiCtlGetValue();			
		}
		configPad.altNote_linked = uiSpinnerNoteAltNote.getLinked();
		if (uiSpinnerNotePressNote.getLinked()) {
			configPad.pressrollNote = uiSpinnerNoteMainNote.uiCtlGetValue();
		} else {
			configPad.pressrollNote = uiSpinnerNotePressNote.uiCtlGetValue();			
		}
		configPad.pressrollNote_linked = uiSpinnerNotePressNote.getLinked();
				
		configPad.channel = uiSpinnerChannel.uiCtlGetValue() - 1;
		configPad.function = uiComboBoxFunction.uiCtlGetValue();
		configPad.curve = uiComboBoxCurve.uiCtlGetValue();
		configPad.compression = uiComboBoxCompression.uiCtlGetValue();
		configPad.shift = uiComboBoxLevelShift.uiCtlGetValue();
		configPad.xtalkLevel = uiComboBoxXTalkLevel.uiCtlGetValue();
		configPad.xtalkGroup = uiComboBoxXTalkGroup.uiCtlGetValue();
		configPad.threshold = uiSpinnerThreshold.uiCtlGetValue();
		configPad.gain = uiComboBoxGain.uiCtlGetValue();
		configPad.autoLevel = uiCheckBoxHighAuto.uiCtlIsSelected();
		configPad.levelMax = uiSpinnerHighLevel.uiCtlGetValue();
		configPad.retrigger = uiSpinnerRetrigger.uiCtlGetValue();
		configPad.dynLevel = uiComboBoxDynLevel.uiCtlGetValue();
		configPad.dynTime = uiComboBoxDynTime.uiCtlGetValue();
		configPad.minScan = uiSpinnerMinScan.uiCtlGetValue();
		configPad.setTypeInt(uiComboBoxType.uiCtlGetValue());
	}

}
