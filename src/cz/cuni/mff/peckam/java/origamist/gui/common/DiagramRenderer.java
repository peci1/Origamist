/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
import com.sun.j3d.exp.swing.JCanvas3D;

import cz.cuni.mff.peckam.java.origamist.exceptions.InvalidOperationException;
import cz.cuni.mff.peckam.java.origamist.exceptions.PaperStructureException;
import cz.cuni.mff.peckam.java.origamist.gui.viewer.DisplayMode;
import cz.cuni.mff.peckam.java.origamist.gui.viewer.StepRendererWithControls;
import cz.cuni.mff.peckam.java.origamist.model.Origami;
import cz.cuni.mff.peckam.java.origamist.model.Step;
import cz.cuni.mff.peckam.java.origamist.utils.LinkedListWithAdditionalBounds;
import cz.cuni.mff.peckam.java.origamist.utils.ListWithAdditionalBounds;
import cz.cuni.mff.peckam.java.origamist.utils.LocalizedString;
import cz.cuni.mff.peckam.java.origamist.utils.ParametrizedCallable;
import cz.cuni.mff.peckam.java.origamist.utils.ParametrizedLocalizedString;

/**
 * A component for rendering a set of StepRenderers.
 * 
 * @author Martin Pecka
 */
public class DiagramRenderer extends JPanelWithOverlay
{
    private static final long                                               serialVersionUID       = -7158217935566060260L;

    /** The origami which is this diagram from. */
    protected Origami                                                       origami;

    /** The first step to be rendered. */
    protected Step                                                          firstStep;

    /** The mode the renderer actually displays the origami in. */
    protected DisplayMode                                                   mode;

    /** The listener that will be fired after the locale of the GUI changed. */
    protected PropertyChangeListener                                        localeListener;

    /** The actually displayed step renderers. */
    protected final List<StepRendererWithControls>                          stepRenderers          = new LinkedList<StepRendererWithControls>();

    /** The pool of step renderers that can be reused. */
    protected final List<StepRendererWithControls>                          stepRendererPool       = new ArrayList<StepRendererWithControls>();
    /** The pool of renderers for empty cells. */
    protected final ListWithAdditionalBounds<JComponent, EmptyCellRenderer> emptyCellRendererPool  = new LinkedListWithAdditionalBounds<JComponent, EmptyCellRenderer>();
    protected final Set<JComponent>                                         usedEmptyCellRenderers = new HashSet<JComponent>();

    /** The basic zoom of all StepRenderers. */
    protected double                                                        zoom                   = 100d;

    /** The listener to be attached to {@link StepRendererWithControls} to listen when the fullscreen is requested. */
    protected PropertyChangeListener                                        stepFullscreenListener;

    // COMPONENTS

    /** The label to be displayed over the renderer while it is loading. */
    protected final JLabel                                                  overlayLabel;

    /** The panel holding all the StepRenderers. */
    protected final JPanel                                                  diagramPane;

    /** The toolbar for diagram manipulations. */
    protected final JToolBarWithBgImage                                     toolbar;

    /** The toolbar of the currently displayed step if the display mode is DIAGRAM. */
    protected JToolBar                                                      stepToolbar;
    /** The step renderer that is the owner of the stepToolbar. */
    protected StepRendererWithControls                                      stepToolBarsRenderer;

    /** The panel holding all toolbars this renderer should show. */
    protected final JPanel                                                  toolbarPane;

    /** The layout for diagramPane in page mode. */
    protected LayoutManager                                                 pageModeLayout         = null;
    /** The layout for diagramPane in diagram mode. */
    protected LayoutManager                                                 diagramModeLayout      = null;

    /** Previous button in PAGE mode. */
    protected final JButton                                                 prevButtonPage;
    /** Previous button in DIAGRAM mode. */
    protected final JButton                                                 prevButtonDiagram;
    /** Next button in PAGE mode. */
    protected final JButton                                                 nextButtonPage;
    /** Next button in DIAGRAM mode. */
    protected final JButton                                                 nextButtonDiagram;
    /** First button in PAGE mode. */
    protected final JButton                                                 firstButtonPage;
    /** First button in DIAGRAM mode. */
    protected final JButton                                                 firstButtonDiagram;
    /** Last button in PAGE mode. */
    protected final JButton                                                 lastButtonPage;
    /** Last button in DIAGRAM mode. */
    protected final JButton                                                 lastButtonDiagram;
    /** The quick-jump combobox for navigating through pages. */
    protected final JComboBox                                               pageSelect;
    /** The model of pageSelect for PAGE mode. */
    protected ComboBoxModel                                                 pageSelectPageModel    = new DefaultComboBoxModel();
    /** The model of pageSelect for DIAGRAM mode. */
    protected ComboBoxModel                                                 pageSelectDiagramModel = new DefaultComboBoxModel();
    /** The string saying "Step x of y" to be displayed in <code>pageSelect</code> */
    protected ParametrizedLocalizedString                                   stepXofY;
    /** The string saying "Page x of y" to be displayed in <code>pageSelect</code> */
    protected ParametrizedLocalizedString                                   pageXofY;

    /**
     * @param o
     * @param firstStep
     */
    public DiagramRenderer(Origami o, Step firstStep)
    {
        this();
        setOrigami(o, firstStep, false);
    }

    /**
     * 
     */
    public DiagramRenderer()
    {
        overlayLabel = new JLocalizedLabel("viewer", "DiagramRenderer.loading");
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
        diagramModeLayout = new FormLayout("fill:default:grow", "fill:default:grow");

        toolbar = new JToolBarWithBgImage("viewer");
        toolbar.setFloatable(false);
        toolbar.setBackground(new Color(231, 231, 184, 230));
        toolbar.setBackgroundImage(new BackgroundImageSupport(getClass()
                .getResource("/resources/images/tooltip-bg.png"), toolbar, 0, 0, BackgroundRepeat.REPEAT_X));

        firstButtonPage = toolbar.createToolbarButton(new FirstAction(), "DiagramRenderer.first.page",
                "first-page-24.png");
        firstButtonDiagram = toolbar.createToolbarButton(new FirstAction(), "DiagramRenderer.first.diagram",
                "first-page-24.png");
        firstButtonDiagram.setVisible(false);

        prevButtonPage = toolbar.createToolbarButton(new PrevAction(), "DiagramRenderer.prev.page", "prev-page-24.png");
        prevButtonDiagram = toolbar.createToolbarButton(new PrevAction(), "DiagramRenderer.prev.diagram",
                "prev-page-24.png");
        prevButtonDiagram.setVisible(false);

        pageSelect = new JComboBox();
        pageSelect.setEditable(false);
        pageSelect.setBackground(new Color(244, 244, 224));
        pageSelect.setAction(new PageSelectAction());

        nextButtonPage = toolbar.createToolbarButton(new NextAction(), "DiagramRenderer.next.page", "next-page-24.png");
        nextButtonDiagram = toolbar.createToolbarButton(new NextAction(), "DiagramRenderer.next.diagram",
                "next-page-24.png");
        nextButtonDiagram.setVisible(false);

        lastButtonPage = toolbar.createToolbarButton(new LastAction(), "DiagramRenderer.last.page", "last-page-24.png");
        lastButtonDiagram = toolbar.createToolbarButton(new LastAction(), "DiagramRenderer.last.diagram",
                "last-page-24.png");
        lastButtonDiagram.setVisible(false);

        toolbarPane = new JPanel();
        toolbarPane.setBorder(BorderFactory.createEmptyBorder());

        buildLayout();

        PropertyChangeListener l = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                if (DisplayMode.DIAGRAM.equals(mode)) {
                    firstButtonDiagram.setEnabled(origami != null
                            && origami.getModel().getSteps().getStep().indexOf(DiagramRenderer.this.firstStep) > 0);
                    prevButtonDiagram.setEnabled(firstButtonDiagram.isEnabled());
                    lastButtonDiagram
                            .setEnabled(origami != null
                                    && origami.getModel().getSteps().getStep().indexOf(DiagramRenderer.this.firstStep) < origami
                                            .getModel().getSteps().getStep().size() - 1);
                    nextButtonDiagram.setEnabled(lastButtonDiagram.isEnabled());
                } else {
                    if (origami != null) {
                        int numSteps = origami.getModel().getSteps().getStep().size();
                        int stepsPerPage = origami.getPaper().getCols() * origami.getPaper().getRows();
                        int numPages = (int) Math.ceil((double) numSteps / stepsPerPage);

                        int stepIndex = origami.getModel().getSteps().getStep().indexOf(DiagramRenderer.this.firstStep) + 1;
                        int pageIndex = (stepIndex - 1) / stepsPerPage + 1;

                        firstButtonPage.setEnabled(pageIndex > 1);
                        prevButtonPage.setEnabled(firstButtonPage.isEnabled());
                        lastButtonPage.setEnabled(pageIndex < numPages);
                        nextButtonPage.setEnabled(lastButtonPage.isEnabled());
                    } else {
                        firstButtonPage.setEnabled(false);
                        prevButtonPage.setEnabled(false);
                        lastButtonPage.setEnabled(false);
                        nextButtonPage.setEnabled(false);
                    }
                }
            }
        };
        addPropertyChangeListener("mode", l);
        addPropertyChangeListener("firstStep", l);

        stepFullscreenListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                if (evt.getSource() instanceof StepRenderer) {
                    setDisplayMode(DisplayMode.DIAGRAM);
                    setStep(((StepRenderer) evt.getSource()).getStep());
                } else if (evt.getSource() instanceof StepRendererWithControls) {
                    setDisplayMode(DisplayMode.DIAGRAM);
                    setStep(((StepRendererWithControls) evt.getSource()).getStep());
                }
            }
        };

        setDisplayMode(DisplayMode.PAGE);
        getContent().setOpaque(false);
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
    protected synchronized void setOrigami(Origami o, Step firstStep, final boolean updateSteps)
    {
        this.origami = o;
        setStep(firstStep);

        int numSteps = o.getModel().getSteps().getStep().size();
        int numPages = o.getNumberOfPages();

        int stepIndex = o.getModel().getSteps().getStep().indexOf(firstStep) + 1;
        int pageIndex = o.getPage(firstStep);

        Object[] steps = new Object[numSteps + 1];
        stepXofY = new ParametrizedLocalizedString("viewer", "DiagramRenderer.stepXofY", stepIndex, numSteps);
        steps[0] = stepXofY;
        for (int i = 1; i <= numSteps; i++)
            steps[i] = i;

        Object[] pages = new Object[numPages + 1];
        pageXofY = new ParametrizedLocalizedString("viewer", "DiagramRenderer.pageXofY", pageIndex, numPages);
        pages[0] = pageXofY;
        for (int i = 1; i <= numPages; i++)
            pages[i] = i;

        pageSelectDiagramModel = new DefaultComboBoxModel(steps);
        pageSelectPageModel = new DefaultComboBoxModel(pages);

        int gridWidth = o.getPaper().getCols();
        StringBuilder colsBuilder = new StringBuilder();
        int[][] colGroups = new int[1][gridWidth];
        for (int i = 0; i < gridWidth; i++) {
            if (i > 0)
                colsBuilder.append(",");
            colsBuilder.append("fill:default:grow");
            colGroups[0][i] = i + 1;
        }

        int gridHeight = o.getPaper().getRows();
        StringBuilder rowsBuilder = new StringBuilder();
        int[][] rowGroups = new int[1][gridHeight];
        for (int i = 0; i < gridHeight; i++) {
            if (i > 0)
                rowsBuilder.append(",");
            rowsBuilder.append("fill:default:grow");
            rowGroups[0][i] = i + 1;
        }

        FormLayout layout = new FormLayout(colsBuilder.toString(), rowsBuilder.toString());
        layout.setColumnGroups(colGroups);
        layout.setRowGroups(rowGroups);
        pageModeLayout = layout;

        int poolSize = o.getPaper().getCols() * o.getPaper().getRows();
        if (stepRendererPool.size() > poolSize) {
            stepRendererPool.subList(poolSize, stepRendererPool.size()).clear();
        } // if the pool should get larger, we don't care here as the new pool entries will be created lazily

        if (emptyCellRendererPool.size() > poolSize) {
            emptyCellRendererPool.subList(poolSize, emptyCellRendererPool.size()).clear();
        } // if the pool should get larger, we don't care here as the new pool entries will be created lazily
        usedEmptyCellRenderers.clear();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run()
            {
                if (mode == DisplayMode.DIAGRAM)
                    pageSelect.setModel(pageSelectDiagramModel);
                else
                    pageSelect.setModel(pageSelectPageModel);

                setBackground(origami.getPaper().getColor().getBackground());

                for (StepRendererWithControls r : stepRendererPool) {
                    if (r != null)
                        r.setOrigami(origami);
                }

                for (EmptyCellRenderer comp : emptyCellRendererPool.getAltView())
                    comp.setOrigami(origami);

                if (updateSteps)
                    updateDisplayedSteps();
            }
        });
    }

    /**
     * Set the given step as the first (or the only) step to be displayed in this renderer.
     * 
     * @param firstStep The first (or the only) step to be displayed in this renderer.
     */
    public synchronized void setStep(Step firstStep)
    {
        Step oldStep = this.firstStep;
        this.firstStep = firstStep;
        firePropertyChange("firstStep", oldStep, firstStep);

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
    public synchronized void setDisplayMode(final DisplayMode mode)
    {
        if (mode.equals(this.mode))
            return;

        DisplayMode oldMode = this.mode;
        this.mode = mode;
        firePropertyChange("mode", oldMode, mode);

        switch (mode) {
            case DIAGRAM:
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
                pageSelect.setModel(pageSelectPageModel);

                if (origami != null) {
                    // when switching back to PAGE mode, don't automatically display the previously displayed step
                    // as the
                    // first step in the page, but make the page begin with a step that corresponds to the first
                    // step of
                    // this page
                    int stepsPerPage = origami.getPaper().getCols() * origami.getPaper().getRows();
                    int stepIndex = origami.getModel().getSteps().getStep().indexOf(firstStep) + 1;
                    int pageIndex = (stepIndex - 1) / stepsPerPage + 1;
                    int newStepIndex = (pageIndex - 1) * stepsPerPage;
                    Step step = origami.getModel().getSteps().getStep().get(newStepIndex);
                    if (step != null)
                        setStep(step);
                }

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
     * This flag will be reset on each {@link #updateDisplayedSteps()} call and will be set to true by the first run
     * error handler. This allows only one error message to be issued for multiple errors.
     */
    protected boolean errorShown = false;

    /**
     * Updates the displayed steps (eg. manages the number of them and repaints).
     */
    protected void updateDisplayedSteps()
    {
        errorShown = false;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run()
            {
                showOverlay();

                diagramPane.removeAll();
                stepRenderers.clear();
                usedEmptyCellRenderers.clear();

                if (origami == null) {
                    hideOverlay();
                    return;
                }

                new Thread() {
                    @Override
                    public void run()
                    {
                        ParametrizedCallable<Void, Exception> errorHandler = new ParametrizedCallable<Void, Exception>() {
                            @Override
                            public Void call(Exception e)
                            {
                                if (errorShown)
                                    return null;
                                if (e instanceof InvalidOperationException) {
                                    errorShown = true;
                                    final InvalidOperationException ioe = (InvalidOperationException) e;
                                    SwingUtilities.invokeLater(new Runnable() {
                                        @Override
                                        public void run()
                                        {
                                            JOptionPane.showMessageDialog(DiagramRenderer.this, ioe
                                                    .getUserFriendlyMessage(), new LocalizedString("application",
                                                    "invalid.operation.title").toString(), JOptionPane.ERROR_MESSAGE);
                                        }
                                    });
                                } else if (e instanceof PaperStructureException) {
                                    errorShown = true;
                                    JOptionPane.showMessageDialog(DiagramRenderer.this, e.getMessage(),
                                            new LocalizedString("application", "invalid.operation.title").toString(),
                                            JOptionPane.ERROR_MESSAGE);
                                }
                                return null;
                            }
                        };

                        final Thread _this = this;
                        if (mode == DisplayMode.DIAGRAM) {
                            final StepRendererWithControls r = getStepRenderer(0);
                            r.setDisplayMode(mode);
                            r.setStep(firstStep, null, errorHandler);
                            r.setZoom(zoom);
                            stepRenderers.add(r);

                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run()
                                {
                                    if (diagramPane.getLayout() != diagramModeLayout)
                                        diagramPane.setLayout(diagramModeLayout);
                                    diagramPane.add(r, new CellConstraints(1, 1));

                                    synchronized (_this) {
                                        _this.notify();
                                    }
                                }
                            });
                        } else {
                            int page = origami.getPage(firstStep);
                            final Integer[] stepsPlacement = origami.getStepsPlacement(page);
                            final int numSteps = stepsPlacement.length;
                            int gridSize = origami.getPaper().getCols() * origami.getPaper().getRows();

                            final JComponent[] panels = new JComponent[gridSize];
                            Arrays.fill(panels, null);
                            // this component will signalize that the cell it lies in is covered by a renderer, but it
                            // isn't its upper left corner
                            final JComponent nonEmptyCell = new JPanel();

                            final int gridWidth = origami.getPaper().getCols();
                            final int gridHeight = origami.getPaper().getRows();

                            Step step = firstStep;
                            for (int i = 0; i < numSteps; i++) {
                                StepRendererWithControls r = getStepRenderer(i);
                                r.setDisplayMode(getDisplayMode());
                                r.setStep(step, null, errorHandler);
                                r.setZoom(zoom);

                                int gridOrigin = stepsPlacement[i];

                                int gridX = gridOrigin % gridWidth;
                                int gridY = gridOrigin / gridWidth;
                                int width = step.getColspan() != null ? step.getColspan() : 1;
                                int height = step.getRowspan() != null ? step.getRowspan() : 1;
                                for (int j = gridX; j < gridX + width; j++) {
                                    for (int k = gridY; k < gridY + height; k++) {
                                        panels[j + gridWidth * k] = nonEmptyCell;
                                    }
                                }
                                panels[gridOrigin] = r;
                                stepRenderers.add(r);

                                step = step.getNext();
                            }
                            for (int i = 0; i < panels.length; i++) {
                                if (panels[i] == null) {
                                    panels[i] = getFreeEmptyCellRenderer();
                                }
                            }
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run()
                                {
                                    if (diagramPane.getLayout() != pageModeLayout)
                                        diagramPane.setLayout(pageModeLayout);

                                    Step step = firstStep;
                                    for (int i = 0; i < numSteps; i++) {
                                        int gridOrigin = stepsPlacement[i];

                                        int x = gridOrigin % gridWidth;
                                        int y = gridOrigin / gridWidth;
                                        int width = step.getColspan() != null ? step.getColspan() : 1;
                                        int height = step.getRowspan() != null ? step.getRowspan() : 1;

                                        diagramPane.add(panels[gridOrigin], new CellConstraints(x + 1, y + 1, width,
                                                height));

                                        step = step.getNext();
                                    }

                                    for (int i = 0; i < panels.length; i++) {
                                        if (panels[i] instanceof EmptyCellRenderer) {
                                            int x = i % gridWidth;
                                            int y = i / gridHeight;
                                            diagramPane.add(panels[i], new CellConstraints(x + 1, y + 1));
                                        }
                                    }

                                    synchronized (_this) {
                                        _this.notify();
                                    }
                                }
                            });
                        }

                        synchronized (this) {
                            try {
                                this.wait(10000);
                            } catch (InterruptedException e) {}
                        }

                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run()
                            {
                                if (stepToolbar != null && stepToolBarsRenderer != null)
                                    stepToolBarsRenderer.getOverlay().add(stepToolbar, new CellConstraints(1, 1));
                                else if (stepToolbar != null)
                                    toolbarPane.remove(stepToolbar);

                                if (mode.equals(DisplayMode.DIAGRAM) && stepRenderers.size() > 0) {
                                    // in DIAGRAM view we want to add the step's toolbar to the toolbar of this renderer
                                    stepToolbar = stepRenderers.get(0).getToolbar();
                                    stepToolBarsRenderer = stepRenderers.get(0);
                                    // also detaches the toolbar from the StepRendererWithControls
                                    toolbarPane.add(stepToolbar);
                                    stepToolbar.setVisible(true);
                                }

                                // this is important!
                                getContent().revalidate();
                                hideOverlay();
                                repaint();

                                new Thread(new Runnable() {
                                    @Override
                                    public void run()
                                    {
                                        stepsUpdated();
                                    }
                                }).start();
                            }
                        });
                    }
                }.start();
            }
        });
    }

    /**
     * Called when steps are resized. Won't be called in EDT.
     */
    protected void stepsUpdated()
    {

    }

    /**
     * Return the step renderer that will draw the step that will be displayed at the given index in this renderer's
     * grid. The returned renderer will have correct origami already set.
     * 
     * @param index The index in this renderer's grid.
     * @return The step renderer.
     */
    protected StepRendererWithControls getStepRenderer(int index)
    {
        // ensure the pool is accordingly large
        while (index + 1 > stepRendererPool.size()) {
            stepRendererPool.add(null);
        }

        if (stepRendererPool.get(index) == null) {
            stepRendererPool.set(index, createStepRenderer());
        }
        return stepRendererPool.get(index);
    }

    /**
     * @return A newly created and setup step renderer.
     */
    protected StepRendererWithControls createStepRenderer()
    {
        final StepRendererWithControls r = new StepRendererWithControls();
        r.setOrigami(origami);
        r.addPropertyChangeListener("fullscreenModeRequested", stepFullscreenListener);
        r.addPropertyChangeListener("zoom", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                if (getDisplayMode() == DisplayMode.DIAGRAM) {
                    zoom = (Double) evt.getNewValue();
                }
            }
        });
        r.getRenderer().getCanvas().setResizeMode(JCanvas3D.RESIZE_DELAYED);
        r.getRenderer().getCanvas().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e)
            {
                revalidate();
                repaint();
            }
        });
        return r;
    }

    /**
     * @return A renderer for empty cells, that isn't a child of a Swing container, so it can be added to a container.
     */
    protected JComponent getFreeEmptyCellRenderer()
    {
        for (JComponent comp : emptyCellRendererPool) {
            if (comp.getParent() == null && !usedEmptyCellRenderers.contains(comp)) {
                usedEmptyCellRenderers.add(comp);
                return comp;
            }
        }

        JComponent newRenderer = createEmptyCellRenderer();

        if (!(newRenderer instanceof EmptyCellRenderer))
            throw new ClassCastException("Empty cell renderer must implement EmptyCellRenderer interface.");

        emptyCellRendererPool.add(newRenderer);
        usedEmptyCellRenderers.add(newRenderer);
        return newRenderer;
    }

    /**
     * @return Create a new renderer for empty cells.
     */
    protected JComponent createEmptyCellRenderer()
    {
        DefaultEmptyCellRenderer result = new DefaultEmptyCellRenderer();
        result.setOrigami(origami);
        return result;
    }

    /**
     * @return the zoom
     */
    public double getZoom()
    {
        return zoom;
    }

    /**
     * @param zoom the zoom to set
     */
    public void setZoom(double zoom)
    {
        final double zoomDelta = zoom - this.zoom;
        this.zoom = zoom;

        for (StepRendererWithControls r : stepRenderers) {
            r.setZoom(r.getZoom() + zoomDelta);
        }

        revalidate();
    }

    /**
     * Increase zoom by 10%.
     */
    public void incZoom()
    {
        setZoom(getZoom() + 10d);
    }

    /**
     * Decrease zoom by 10%.
     */
    public void decZoom()
    {
        setZoom(getZoom() - 10d);
    }

    /**
     * Switch to the selected step/page (depends on the current display mode).
     * 
     * @author Martin Pecka
     */
    class PageSelectAction extends AbstractAction
    {

        /** */
        private static final long serialVersionUID = -3468061316473710494L;

        @Override
        public void actionPerformed(ActionEvent e)
        {
            final Object selectedObject = pageSelect.getSelectedItem();
            if (selectedObject instanceof Integer) {
                final Integer index = (Integer) selectedObject;
                final Step step;

                if (getDisplayMode().equals(DisplayMode.DIAGRAM)) {
                    step = origami.getModel().getSteps().getStep().get(index - 1);
                } else {
                    step = origami.getFirstStep(index);
                }

                if (step != null) {
                    setStep(step);
                    updateDisplayedSteps();
                }
            }
            pageSelect.setSelectedIndex(0);
        }

    }

    /**
     * Sets the first step/page as the step/page to be displayed.
     * 
     * @author Martin Pecka
     */
    class FirstAction extends AbstractAction
    {
        /** */
        private static final long serialVersionUID = -4456533603941034141L;

        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (getDisplayMode().equals(DisplayMode.DIAGRAM) && !firstButtonDiagram.isEnabled())
                return;
            else if (getDisplayMode().equals(DisplayMode.PAGE) && !firstButtonPage.isEnabled())
                return;

            setStep(origami.getModel().getSteps().getStep().get(0));
            updateDisplayedSteps();
        }
    }

    /**
     * Sets the last step/page as the step/page to be displayed.
     * 
     * @author Martin Pecka
     */
    class LastAction extends AbstractAction
    {
        /** */
        private static final long serialVersionUID = -4456533603941034141L;

        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (getDisplayMode().equals(DisplayMode.DIAGRAM)) {
                if (!lastButtonDiagram.isEnabled())
                    return;
                setStep(origami.getModel().getSteps().getStep().get(origami.getModel().getSteps().getStep().size() - 1));
            } else {
                if (!lastButtonPage.isEnabled())
                    return;
                int numPages = origami.getNumberOfPages();
                Step step = origami.getFirstStep(numPages);
                setStep(step);
            }
            updateDisplayedSteps();
        }
    }

    /**
     * Sets the previous step/page as the step/page to be displayed.
     * 
     * @author Martin Pecka
     */
    class PrevAction extends AbstractAction
    {
        /** */
        private static final long serialVersionUID = -7796794346643659044L;

        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (getDisplayMode().equals(DisplayMode.DIAGRAM)) {
                if (!prevButtonDiagram.isEnabled())
                    return;
                int index = origami.getModel().getSteps().getStep().indexOf(firstStep);
                if (index > 0) {
                    setStep(origami.getModel().getSteps().getStep().get(index - 1));
                    updateDisplayedSteps();
                }
            } else {
                if (!prevButtonPage.isEnabled())
                    return;

                int page = origami.getPage(firstStep);
                page--;
                if (page > 0) {
                    Step step = origami.getFirstStep(page);
                    setStep(step);
                    updateDisplayedSteps();
                }
            }
        }
    }

    /**
     * Sets the next step/page as the step/page to be displayed.
     * 
     * @author Martin Pecka
     */
    class NextAction extends AbstractAction
    {
        /** */
        private static final long serialVersionUID = 6137420423152101455L;

        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (getDisplayMode().equals(DisplayMode.DIAGRAM)) {
                if (!nextButtonDiagram.isEnabled())
                    return;
                int index = origami.getModel().getSteps().getStep().indexOf(firstStep);
                if (index < origami.getModel().getSteps().getStep().size() - 1) {
                    setStep(origami.getModel().getSteps().getStep().get(index + 1));
                    updateDisplayedSteps();
                }
            } else {
                if (!nextButtonPage.isEnabled())
                    return;
                int page = origami.getPage(firstStep);
                int numPages = origami.getNumberOfPages();

                if (page < numPages) {
                    page++;
                    Step step = origami.getFirstStep(page);
                    setStep(step);
                    updateDisplayedSteps();
                }
            }
        }
    }

    /**
     * A renderer of empty cells.
     * 
     * @author Martin Pecka
     */
    public interface EmptyCellRenderer
    {
        /**
         * Set the origami this renderer should render empty cell for.
         * 
         * @param o
         */
        void setOrigami(Origami o);
    }

    /**
     * Empty cell renderer implemented by a JPanel.
     * 
     * @author Martin Pecka
     */
    protected class DefaultEmptyCellRenderer extends JPanel implements EmptyCellRenderer
    {
        /** */
        private static final long serialVersionUID = -7852465401961605356L;

        @Override
        public void setOrigami(Origami o)
        {
            if (o != null)
                setBackground(o.getPaper().getBackgroundColor());
        }

    }
}
