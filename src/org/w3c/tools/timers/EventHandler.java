// EventHandler.java
// $Id: EventHandler.java,v 1.2 1998/01/22 13:09:59 bmahe Exp $
// (c) COPYRIGHT MIT and INRIA, 1996.
// Please first read the full copyright statement in file COPYRIGHT.html

// Origamist: http://www.ms.mff.cuni.cz/~peckam/origamist
//
// Copyright © 2011 World Wide Web Consortium, (Massachusetts Institute of Technology, European Research Consortium for
// Informatics and Mathematics, Keio University). All Rights Reserved. This work is distributed under the W3C® Software
// License [1] in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
//
// [1] http://www.w3.org/Consortium/Legal/2002/copyright-software-20021231

package org.w3c.tools.timers;

public interface EventHandler
{
    /**
     * Handle an timer event. Data is the rock the register wanted
     * passed to the handleEvent routine, and time is the time the
     * event was scheduled to be delivered. The handler can compare
     * that time to the current time, if it wants, to see whether or
     * not it's falling behind.
     */
    void handleTimerEvent(Object data, long time);
}
