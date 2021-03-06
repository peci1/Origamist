/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.viewer;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.prefs.BackingStoreException;

import javax.swing.AbstractAction;
import javax.swing.origamist.JLocalizedLabel;

import org.apache.log4j.Logger;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import cz.cuni.mff.peckam.java.origamist.common.LangString;
import cz.cuni.mff.peckam.java.origamist.exceptions.UnsupportedDataFormatException;
import cz.cuni.mff.peckam.java.origamist.files.CategoriesContainer;
import cz.cuni.mff.peckam.java.origamist.files.Category;
import cz.cuni.mff.peckam.java.origamist.files.File;
import cz.cuni.mff.peckam.java.origamist.gui.common.AbstractSettingsFrame;
import cz.cuni.mff.peckam.java.origamist.model.Origami;
import cz.cuni.mff.peckam.java.origamist.model.jaxb.Unit;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.ConfigurationManager;

/**
 * The window with the settings of the viewer.
 * 
 * @author Martin Pecka
 */
public class SettingsFrame extends AbstractSettingsFrame
{

    /** */
    private static final long serialVersionUID = 5672395004264190956L;

    public SettingsFrame()
    {
        okBtn.setAction(new OKAction());
    }

    @Override
    protected void buildLayout()
    {
        setLayout(new FormLayout("$dm,left:pref,$lcgap,pref:grow,right:pref,$dm",
                "$dm,pref,$ugap,pref,$ugap,pref,$ugap,pref,$dm"));
        CellConstraints cc = new CellConstraints();

        add(new JLocalizedLabel("application", "appLocale"), cc.xy(2, 2));
        add(appLocaleComboBox, cc.xyw(4, 2, 2));

        add(new JLocalizedLabel("application", "diagramLocale"), cc.xy(2, 4));
        add(diagramLocaleComboBox, cc.xyw(4, 4, 2));

        add(new JLocalizedLabel("application", "preferredUnit"), cc.xy(2, 6));
        add(preferredUnitComboBox, cc.xyw(4, 6, 2));

        add(okBtn, cc.xy(5, 8));
        add(cancelBtn, cc.xy(2, 8));
    }

    @Override
    protected LinkedHashSet<Locale> getDiagramSuggestedLocales()
    {
        LinkedHashSet<Locale> result = new LinkedHashSet<Locale>();

        result.add(ServiceLocator.get(ConfigurationManager.class).get().getDiagramLocale());
        result.add(ServiceLocator.get(ConfigurationManager.class).get().getLocale());
        result.add(Locale.getDefault());

        Iterator<? extends CategoriesContainer> ic = ServiceLocator.get(OrigamiViewer.class).filesToDisplay
                .recursiveCategoryIterator(false);
        while (ic.hasNext()) {
            Category cat = (Category) ic.next();
            for (LangString ls : cat.getName())
                result.add(ls.getLang());
        }

        Iterator<File> itf = ServiceLocator.get(OrigamiViewer.class).filesToDisplay.recursiveFileIterator();
        while (itf.hasNext()) {
            File f = itf.next();
            for (LangString ls : f.getName())
                result.add(ls.getLang());
            for (LangString ls : f.getShortdesc())
                result.add(ls.getLang());
            if (f.isOrigamiLoaded()) {
                try {
                    Origami o = f.getOrigami();
                    for (LangString ls : o.getDescription())
                        result.add(ls.getLang());
                    for (LangString ls : o.getName())
                        result.add(ls.getLang());
                    for (LangString ls : o.getShortdesc())
                        result.add(ls.getLang());
                } catch (UnsupportedDataFormatException e) {
                    assert false : "Origami loaded but getOrigami() threw exception.";
                } catch (IOException e) {
                    assert false : "Origami loaded but getOrigami() threw exception.";
                }
            }
        }

        return result;
    }

    /**
     * Action to apply changes made in this dialog and close it.
     * 
     * @author Martin Pecka
     */
    class OKAction extends AbstractAction
    {

        /** */
        private static final long serialVersionUID = -2245759319491457479L;

        @Override
        public void actionPerformed(ActionEvent e)
        {
            ServiceLocator.get(ConfigurationManager.class).get()
                    .setLocale((Locale) appLocaleComboBox.getSelectedItem());
            ServiceLocator.get(ConfigurationManager.class).get()
                    .setDiagramLocale((Locale) diagramLocaleComboBox.getSelectedItem());
            ServiceLocator.get(ConfigurationManager.class).get()
                    .setPreferredUnit((Unit) preferredUnitComboBox.getSelectedItem());
            try {
                ServiceLocator.get(ConfigurationManager.class).persist();
            } catch (BackingStoreException e1) {
                Logger.getLogger("application").warn("Couldn't save configuration", e1);
            }
            setVisible(false);
        }

    }
}
