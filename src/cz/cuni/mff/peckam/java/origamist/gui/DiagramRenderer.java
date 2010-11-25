/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BackgroundImageSupport;
import javax.swing.BackgroundImageSupport.BackgroundRepeat;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPanelWithOverlay;
import javax.swing.JToolBarWithBgImage;
import javax.swing.SwingUtilities;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import cz.cuni.mff.peckam.java.origamist.gui.viewer.DisplayMode;
import cz.cuni.mff.peckam.java.origamist.model.Origami;
import cz.cuni.mff.peckam.java.origamist.model.Step;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.ConfigurationManager;

/**
 * A component for rendering a set of StepRenderers.
 * 
 * @author Martin Pecka
 */
public class DiagramRenderer extends JPanelWithOverlay
{
    private static final long        serialVersionUID    = -7158217935566060260L;

    /** The origami which is this diagram from. */
    protected Origami                origami;

    /** The first step to be rendered. */
    protected Step                   firstStep;

    /** A display pattern for displaying only a single step. */
    protected static final Point     SINGLE_STEP_PATTERN = new Point(1, 1);

    /** The number of steps that will be shown at once. */
    protected Point                  displayedStepsPattern;

    /** The mode the renderer actually displays the origami in. */
    protected DisplayMode            mode;

    /** The panel holding all the StepRenderers. */
    protected JPanel                 diagramPane         = new JPanel();

    /** The toolbar for diagram manipulations. */
    protected JToolBarWithBgImage    toolbar             = new JToolBarWithBgImage();

    /** The listener that will be fired after the locale of the GUI changed. */
    protected PropertyChangeListener localeListener;

    public DiagramRenderer(Origami o, Step firstStep)
    {
        getOverlay().setLayout(
                new FormLayout("left:pref:grow, center:pref, right:pref:grow",
                        "top:pref:grow, center:pref, bottom:pref:grow"));
        final JLabel label = new JLabel();
        label.setFont(label.getFont().deriveFont(Font.BOLD, 40));
        label.setOpaque(false);
        getOverlay().add(label, new CellConstraints(2, 2));
        getOverlay().setBackground(new Color(0, 0, 0, 64));
        // make the overlay "inert" to mouse
        getOverlay().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e)
            {
            }

            @Override
            public void mousePressed(MouseEvent e)
            {
            }

            @Override
            public void mouseReleased(MouseEvent e)
            {
            }

            @Override
            public void mouseMoved(MouseEvent e)
            {
            }
        });

        getContent().setLayout(new BorderLayout(0, 1));
        getContent().add(diagramPane, BorderLayout.CENTER);

        toolbar.setFloatable(false);
        toolbar.setBackground(new Color(231, 231, 184, 230));
        toolbar.setBackgroundImage(new BackgroundImageSupport(getClass()
                .getResource("/resources/images/tooltip-bg.png"), toolbar, 0, 0, BackgroundRepeat.REPEAT_X));

        final JButton prevButton = new JButton();
        toolbar.add(prevButton);
        final JButton nextButton = new JButton();
        toolbar.add(nextButton);
        getContent().add(toolbar, BorderLayout.PAGE_END);

        localeListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                ResourceBundle messages = ResourceBundle.getBundle("application", (Locale) evt.getNewValue());
                label.setText(messages.getString("DiagramRenderer.loading"));
                if (getDisplayMode().equals(DisplayMode.PAGE)) {
                    prevButton.setText(messages.getString("DiagramRenderer.prev.page"));
                    nextButton.setText(messages.getString("DiagramRenderer.next.page"));
                } else {
                    prevButton.setText(messages.getString("DiagramRenderer.prev.diagram"));
                    nextButton.setText(messages.getString("DiagramRenderer.next.diagram"));
                }
            }
        };
        ServiceLocator.get(ConfigurationManager.class).get().addPropertyChangeListener("locale", localeListener);

        setOrigami(o, firstStep, false);
        // also runs the localeListener
        setDisplayMode(DisplayMode.PAGE);
    }

    /**
     * Instructs the renderer to display the given origami from now on.
     * 
     * Automatically chooses the first step to be displayed.
     * 
     * @param o The origami to display.
     */
    public synchronized void setOrigami(Origami o)
    {
        setOrigami(o, o.getModel().getSteps().getStep().get(0));
    }

    /**
     * Instructs the renderer to display the given origami from the given step.
     * 
     * @param o The origami to display.
     * @param firstStep The step to be displayed as the first one.
     */
    public synchronized void setOrigami(Origami o, Step firstStep)
    {
        setOrigami(o, firstStep, true);
    }

    /**
     * Instructs the renderer to display the given origami from the given step.
     * 
     * @param o The origami to display.
     * @param firstStep The step to be displayed as the first one.
     * @param updateSteps Whether to update the displayed steps, or delay it for later.
     */
    protected synchronized void setOrigami(Origami o, Step firstStep, boolean updateSteps)
    {
        this.origami = o;
        this.firstStep = firstStep;

        if (updateSteps)
            updateDisplayedSteps();
    }

    /**
     * Set the display mode to the one given. If the new mode is different from the actual one, a redraw will be forced.
     * 
     * @param mode The new mode to switch the renderer to.
     */
    public synchronized void setDisplayMode(DisplayMode mode)
    {
        if (mode.equals(this.mode) && displayedStepsPattern != null)
            return;

        this.mode = mode;

        switch (mode) {
            case DIAGRAM:
                displayedStepsPattern = SINGLE_STEP_PATTERN;
                break;
            case PAGE:
                displayedStepsPattern = new Point(origami.getPaper().getCols(), origami.getPaper().getRows());
                break;
        }
        updateDisplayedSteps();
        localeListener.propertyChange(new PropertyChangeEvent(this, "locale", null, ServiceLocator
                .get(ConfigurationManager.class).get().getLocale()));
    }

    /**
     * @return The mode the renderer actually displays the origami in.
     */
    public DisplayMode getDisplayMode()
    {
        return mode;
    }

    /**
     * Updates the displayed steps (eg. manages the number of them and repaints).
     */
    protected void updateDisplayedSteps()
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run()
            {
                showOverlay();
                diagramPane.setLayout(new GridLayout(displayedStepsPattern.y, displayedStepsPattern.x));

                // this is needed for performance. Also Java3D cannot handle more than 32 JCanvas3D's at once.
                diagramPane.removeAll();

                new Thread() {
                    @Override
                    public void run()
                    {
                        int numSteps = displayedStepsPattern.x * displayedStepsPattern.y;
                        Step step = firstStep;
                        for (int i = 0; i < numSteps; i++) {
                            StepRenderer r = new StepRenderer(origami, step);
                            diagramPane.add(r);
                            if (step.getNext() != null)
                                step = step.getNext();
                            else
                                break;
                        }
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run()
                            {
                                getContent().revalidate();
                                hideOverlay();
                            }
                        });
                    }
                }.start();
            }
        });
    }
}
