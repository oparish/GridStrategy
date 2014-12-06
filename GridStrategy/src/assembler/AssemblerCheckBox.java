package assembler;

import javax.swing.JCheckBox;

public class AssemblerCheckBox extends JCheckBox
{
	PanelControl panelControl;
	
	public PanelControl getPanelControl() {
		return panelControl;
	}

	AssemblerCheckBox(PanelControl panelControl)
	{
		super();
		this.panelControl = panelControl;
	}
}
