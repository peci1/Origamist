/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.origamist.BackgroundImageSupport;
import javax.swing.origamist.BackgroundImageSupport.BackgroundRepeat;
import javax.swing.origamist.JLocalizedLabel;
import javax.swing.origamist.JPanelWithOverlay;
import javax.swing.origamist.JToolBarWithBgImage;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import cz.cuni.mff.peckam.java.origamist.gui.viewer.DisplayMode;
import cz.cuni.mff.peckam.java.origamist.model.Origami;
import cz.cuni.mff.peckam.java.origamist.model.Step;
import cz.cuni.mff.peckam.java.origamist.utils.ParametrizedLocalizedString;

/**
 * A component for rendering a set of StepRenderers.
 * 
 * @author Martin Pecka
 */
public class DiagramRenderer extends JPanelWithOverlay
{
    private static final long             serialVersionUID    = -7158217935566060260L;

    /** The origami which is this diagram from. */
    protected Origami                     origami;

    /** The first step to be rendered. */
    protected Step                        firstStep;

    /** A display pattern for displaying only a single step. */
    protected static final Dimension      SINGLE_STEP_PATTERN = new Dimension(1, 1);

    /** The number of steps that will be shown at once. */
    protected Dimension                   displayedStepsPattern;

    /** The mode the renderer actually displays the origami in. */
    protected DisplayMode                 mode;

    /** The listener that will be fired after the locale of the GUI changed. */
    protected PropertyChangeListener      localeListener;

    /** The actually displayed step renderers. */
    protected final List<StepRenderer>    stepRenderers       = new LinkedList<StepRenderer>();

    // COMPONENTS

    /** The label to be displayed over the renderer while it is loading. */
    protected final JLabel                overlayLabel;

    /** The panel holding all the StepRenderers. */
    protected final JPanel                diagramPane;

    /** The toolbar for diagram manipulations. */
    protected final JToolBarWithBgImage   toolbar;

    /** The toolbar of the currently displayed step if the display mode is DIAGRAM. */
    protected JToolBar                    stepToolbar;

    /** The panel holding all toolbars this renderer should show. */
    protected final JPanel                toolbarPane;

    /** Previous button in PAGE mode. */
    protected final JButton               prevButtonPage;
    /** Previous button in DIAGRAM mode. */
    protected final JButton               prevButtonDiagram;
    /** Next button in PAGE mode. */
    protected final JButton               nextButtonPage;
    /** Next button in DIAGRAM mode. */
    protected final JButton               nextButtonDiagram;
    /** First button in PAGE mode. */
    protected final JButton               firstButtonPage;
    /** First button in DIAGRAM mode. */
    protected final JButton               firstButtonDiagram;
    /** Last button in PAGE mode. */
    protected final JButton               lastButtonPage;
    /** Last button in DIAGRAM mode. */
    protected final JButton               lastButtonDiagram;
    /** The quick-jump combobox for navigating through pages. */
    protected final JComboBox             pageSelect;
    /** The model of pageSelect for PAGE mode. */
    protected ComboBoxModel               pageSelectPageModel;
    /** The model of pageSelect for DIAGRAM mode. */
    protected ComboBoxModel               pageSelectDiagramModel;
    /** The string saying "Step x of y" to be displayed in <code>pageSelect</code> */
    protected ParametrizedLocalizedString stepXofY;
    /** The string saying "Page x of y" to be displayed in <code>pageSelect</code> */
    protected ParametrizedLocalizedString pageXofY;

    public DiagramRenderer(Origami o, Step firstStep)
    {
        overlayLabel = new JLocalizedLabel("application", "DiagramRenderer.loading");
        overlayLabel.setFont(overlayLabel.getFont().deriveFont(Font.BOLD, 40));
        overlayLabel.setOpaque(false);

        getOverlay().setBackground(new Color(0, 0, 0, 64));
        // don't allow the content to respond to key events while the overlay is displayed
        getOverlay().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e)
            {
                e.consume();
            }

            @Override
            public void keyPressed(KeyEvent e)
            {
                e.consume();
            }

            @Override
            public void keyReleased(KeyEvent e)
            {
                e.consume();
            }
        });
        // don't allow the content to respond to mouse events while the overlay is displayed
        getOverlay().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                e.consume();
            }

            @Override
            public void mousePressed(MouseEvent e)
            {
                e.consume();
            }

            @Override
            public void mouseReleased(MouseEvent e)
            {
                e.consume();
            }

            @Override
            public void mouseMoved(MouseEvent e)
            {
                e.consume();
            }
        });

        diagramPane = new JPanel();

        toolbar = new JToolBarWithBgImage();
        toolbar.setFloatable(false);
        toolbar.setBackground(new Color(231, 231, 184, 230));
        toolbar.setBackgroundImage(new BackgroundImageSupport(getClass()
                .getResource("/resources/images/tooltip-bg.png"), toolbar, 0, 0, BackgroundRepeat.REPEAT_X));

        firstButtonPage = toolbar.createToolbarButton(null, "DiagramRenderer.first.page", "first-page-24.png");
        firstButtonDiagram = toolbar.createToolbarButton(null, "DiagramRenderer.first.diagram", "first-page-24.png");
        firstButtonDiagram.setVisible(false);

        prevButtonPage = toolbar.createToolbarButton(null, "DiagramRenderer.prev.page", "prev-page-24.png");
        prevButtonDiagram = toolbar.createToolbarButton(null, "DiagramRenderer.prev.diagram", "prev-page-24.png");
        prevButtonDiagram.setVisible(false);

        pageSelect = new JComboBox();
        pageSelect.setEditable(false);

        nextButtonPage = toolbar.createToolbarButton(null, "DiagramRenderer.next.page", "next-page-24.png");
        nextButtonDiagram = toolbar.createToolbarButton(null, "DiagramRenderer.next.diagram", "next-page-24.png");
        nextButtonDiagram.setVisible(false);

        lastButtonPage = toolbar.createToolbarButton(null, "DiagramRenderer.last.page", "last-page-24.png");
        lastButtonDiagram = toolbar.createToolbarButton(null, "DiagramRenderer.last.diagram", "last-page-24.png");
        lastButtonDiagram.setVisible(false);

        toolbarPane = new JPanel();
        toolbarPane.setBorder(BorderFactory.createEmptyBorder());

        buildLayout();

        setOrigami(o, firstStep, false);
        setDisplayMode(DisplayMode.PAGE);
    }

    /**
     * Add the components to their containers.
     */
    protected void buildLayout()
    {
        getOverlay().setLayout(
                new FormLayout("left:pref:grow, center:pref, right:pref:grow",
                        "top:pref:grow, center:pref, bottom:pref:grow"));

        getOverlay().add(overlayLabel, new CellConstraints(2, 2));

        getContent().setLayout(new BorderLayout(0, 1));
        getContent().add(diagramPane, BorderLayout.CENTER);
        getContent().add(toolbarPane, BorderLayout.PAGE_END);

        toolbar.add(firstButtonPage);
        toolbar.add(firstButtonDiagram);
        toolbar.add(prevButtonPage);
        toolbar.add(prevButtonDiagram);
        toolbar.add(pageSelect);
        toolbar.add(nextButtonPage);
        toolbar.add(nextButtonDiagram);
        toolbar.add(lastButtonPage);
        toolbar.add(lastButtonDiagram);

        toolbarPane.setLayout(new FlowLayout(FlowLayout.LEADING, 5, 0));
        toolbarPane.add(toolbar);

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
        setStep(firstStep);

        int numSteps = o.getModel().getSteps().getStep().size();
        int stepsPerPage = o.getPaper().getCols() * o.getPaper().getRows();
        int numPages = (int) Math.ceil((double) numSteps / stepsPerPage);

        int stepIndex = o.getModel().getSteps().getStep().indexOf(firstStep) + 1;
        int pageIndex = (stepIndex - 1) / stepsPerPage + 1;

        Object[] steps = new Object[numSteps + 1];
        stepXofY = new ParametrizedLocalizedString("application", "DiagramRenderer.stepXofY", stepIndex, numSteps);
        steps[0] = stepXofY;
        for (int i = 1; i <= numSteps; i++)
            steps[i] = i;

        Object[] pages = new Object[numPages + 1];
        pageXofY = new ParametrizedLocalizedString("application", "DiagramRenderer.pageXofY", pageIndex, numPages);
        pages[0] = pageXofY;
        for (int i = 1; i <= numPages; i++)
            pages[i] = i;

        pageSelectDiagramModel = new DefaultComboBoxModel(steps);
        pageSelectPageModel = new DefaultComboBoxModel(pages);

        if (updateSteps)
            updateDisplayedSteps();
    }

    /**
     * Set the given step as the first (or the only) step to be displayed in this renderer.
     * 
     * @param firstStep The first (or the only) step to be displayed in this renderer.
     */
    public synchronized void setStep(Step firstStep)
    {
        this.firstStep = firstStep;

        if (stepXofY != null && pageXofY != null) {
            int numSteps = origami.getModel().getSteps().getStep().size();
            int stepsPerPage = origami.getPaper().getCols() * origami.getPaper().getRows();
            int numPages = (int) Math.ceil((double) numSteps / stepsPerPage);

            int stepIndex = origami.getModel().getSteps().getStep().indexOf(firstStep) + 1;
            int pageIndex = (stepIndex - 1) / stepsPerPage + 1;

            stepXofY.setParameters(stepIndex, numSteps);
            pageXofY.setParameters(pageIndex, numPages);
        }
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
                pageSelect.setModel(pageSelectDiagramModel);

                prevButtonDiagram.setVisible(true);
                prevButtonPage.setVisible(false);
                nextButtonDiagram.setVisible(true);
                nextButtonPage.setVisible(false);
                firstButtonDiagram.setVisible(true);
                firstButtonPage.setVisible(false);
                lastButtonDiagram.setVisible(true);
                lastButtonPage.setVisible(false);
                break;
            case PAGE:
                displayedStepsPattern = new Dimension(origami.getPaper().getCols(), origami.getPaper().getRows());
                pageSelect.setModel(pageSelectPageModel);

                prevButtonDiagram.setVisible(false);
                prevButtonPage.setVisible(true);
                nextButtonDiagram.setVisible(false);
                nextButtonPage.setVisible(true);
                firstButtonDiagram.setVisible(false);
                firstButtonPage.setVisible(true);
                lastButtonDiagram.setVisible(false);
                lastButtonPage.setVisible(true);
                break;
        }
        updateDisplayedSteps();
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

                diagramPane.removeAll();
                stepRenderers.clear();

                diagramPane.setLayout(new GridLayout(displayedStepsPattern.height, displayedStepsPattern.width));

                new Thread() {
                    @Override
                    public void run()
                    {
                        int numSteps = displayedStepsPattern.width * displayedStepsPattern.height;
                        Step step = firstStep;
                        for (int i = 0; i < numSteps; i++) {
                            StepRenderer r = new StepRenderer(origami, step);
                            r.setDisplayMode(getDisplayMode());
                            diagramPane.add(r);
                            stepRenderers.add(r);
                            if (step.getNext() != null)
                                step = step.getNext();
                            else
                                break;
                        }

                        if (stepRenderers.size() == 0)
                            return; // TODO maybe tell the user that nothing is to be displayed

                        if (mode.equals(DisplayMode.DIAGRAM)) {
                            // in DIAGRAM view we want to add the step's toolbar to the toolbar of this renderer
                            stepToolbar = stepRenderers.get(0).getToolbar();
                            // also detaches the toolbar from the StepRenderer
                            toolbarPane.add(stepToolbar);
                            stepToolbar.setVisible(true);
                        } else {
                            // in the PAGE view we don't want to mix a step's toolbar with the main toolbar
                            if (stepToolbar != null)
                                toolbarPane.remove(stepToolbar);
                        }
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run()
                            {
                                // this is important!
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
