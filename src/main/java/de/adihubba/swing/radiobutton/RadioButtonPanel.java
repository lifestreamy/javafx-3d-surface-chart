package de.adihubba.swing.radiobutton;

import de.adihubba.ObjectUtils;
import de.adihubba.swing.ListLayout;
import de.adihubba.swing.ValueChangedListener;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * RadioButtonPanel
 */
public abstract class RadioButtonPanel extends JPanel {

    protected final ButtonGroup buttonGroup;
    protected final boolean allowDeselection;
    private final List<JRadioButton> buttons;
    private final int orientation;
    private final Set<ValueChangedListener> valueChangedListeners = new HashSet<>();
    private Boolean lastValue;

    public RadioButtonPanel() {
        this(SwingConstants.HORIZONTAL, false);
    }

    /**
     * @param allowDeselection if true will allow selected choices to be deselected.
     * @param orientation      layout buttons in a row (SwingConstants.HORIZONTAL) or a column (SwingConstants.VERTICAL)
     */
    public RadioButtonPanel(int orientation, boolean allowDeselection) {
        this.buttons = createButtons();
        this.orientation = orientation;
        this.allowDeselection = allowDeselection;

        initComponents();
        initModels();

        buttonGroup = new ButtonGroup();
        for (JRadioButton button : buttons) {
            buttonGroup.add(button);
        }
        initListeners();
    }

    private void initListeners() {
        this.addActionListener(e -> handleValueChanged());
    }

    private void initModels() {
        if (allowDeselection) {
            for (JRadioButton button : buttons) {
                button.setModel(new SelectableToggleButtonModel());
            }
        }
    }

    protected void deselect() {
        this.buttonGroup.clearSelection();
    }

    public void addActionListener(ActionListener listener) {
        for (JRadioButton button : buttons) {
            button.addActionListener(listener);
        }
    }

    private void handleValueChanged() {
        //no listeners
        if (valueChangedListeners.isEmpty()) {
            return;
        }

        final Boolean oldValue = getLastValue();
        final Boolean newValue = getValue();

        if (valueChanged(oldValue, newValue)) {
            notifyValueChangedListeners(oldValue, newValue);
            setLastValue(newValue);
        }
    }

    private void notifyValueChangedListeners(Boolean oldValue, Boolean newValue) {
        for (ValueChangedListener listener : valueChangedListeners) {
            listener.valueChanged(oldValue, newValue, this);
        }
    }

    abstract protected Boolean getValue();

    private boolean valueChanged(Boolean oldValue, Boolean newValue) {
        return !ObjectUtils.equalsObject(oldValue, newValue);
    }

    private Boolean getLastValue() {
        return this.lastValue;
    }

    private void setLastValue(Boolean value) {
        this.lastValue = value;
    }

    public final void addValueChangedListener(ValueChangedListener listener) {
        if (listener != null) {
            valueChangedListeners.add(listener);
        }
    }

    public void removeActionListener(ActionListener listener) {
        for (JRadioButton button : buttons) {
            button.removeActionListener(listener);
        }
    }

    protected void initComponents() {
        if (orientation == SwingConstants.HORIZONTAL) {
            setLayout(new ListLayout(SwingConstants.HORIZONTAL, 5));

            for (JRadioButton button : buttons) {
                add(button);
            }
        }

        if (orientation == SwingConstants.VERTICAL) {
            setLayout(new ListLayout());

            for (JRadioButton button : buttons) {
                add(button);
            }
        }
    }

    abstract protected List<JRadioButton> createButtons();

    static public class SelectableToggleButtonModel extends JToggleButton.ToggleButtonModel {

        @Override
        public void setSelected(boolean select) {
            if (!select) {
                ButtonGroup buttonGroup = getGroup();
                if (buttonGroup != null && buttonGroup.isSelected(this)) {
                    // if deselecting a selected button, remove the group selection
                    buttonGroup.clearSelection();
                    return;
                }
            }

            super.setSelected(select);
        }

    }

}
