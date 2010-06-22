/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.utils;

import java.security.Permission;

/**
 * Provides conversion between Java Permission and string permission
 * representaions
 * 
 * @author Martin Pecka
 */
public class PermissionConverter
{
    /**
     * Parse Java Permission from permission string representation
     * 
     * @param s
     * @return locale
     */
    public static Permission parse(String s)
    {
        return new Permission(s) {
            private static final long serialVersionUID = -1061029931943379601L;

            @Override
            public boolean implies(Permission permission)
            {
                return false;
            }

            @Override
            public int hashCode()
            {
                return getName().hashCode();
            }

            @Override
            public String getActions()
            {
                return getName();
            }

            @Override
            public boolean equals(Object obj)
            {
                if (obj instanceof Permission) {
                    return ((Permission) obj).getName().equals(getName());
                }
                return false;
            }
        };
    }

    /**
     * Parse string permission representaion from Java Permission
     * 
     * @param l
     * @return the string representation of the locale
     */
    public static String print(Permission l)
    {
        return l.getName();
    }
}
