/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;

/**
 *
 * @author IT_WannaBe
 */
public class AttendantEventId implements Serializable {

    private long attendantId;

    private long eventId;

    public int hashCode() {
        return (int) (attendantId + eventId);
    }

    public boolean equals(Object object) {
        if (object instanceof AttendantEventId) {
            AttendantEventId otherId = (AttendantEventId) object;
            return (otherId.attendantId == this.attendantId) && (otherId.eventId == this.eventId);
        }
        return false;
    }
}
