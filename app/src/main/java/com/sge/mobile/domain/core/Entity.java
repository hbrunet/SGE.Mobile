package com.sge.mobile.domain.core;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 05/07/13.
 */
public abstract class Entity {
    protected List<String> validationResults;
    private Integer requestedHashCode;

    protected Entity() {
        this.validationResults = new ArrayList<String>();
    }

    public abstract int getId();

    public abstract void setId(int id);

    public boolean isNew() {
        return this.getId() == 0;
    }

    public boolean isValid() {
        this.validationResults.clear();
        this.validate();
        return this.validationResults.size() == 0;
    }

    public boolean sameIdentityAs(final Entity other) {
        return other.getId() == this.getId();
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) return true;
        if (object == null || !(object instanceof Entity)) return false;

        final Entity other = (Entity) object;
        return this.sameIdentityAs(other);
    }

    @Override
    public int hashCode() {
        if (this.isNew()) {
            if (requestedHashCode == null)
                requestedHashCode = this.getId() ^ 31;

            return requestedHashCode;
        } else
            return super.hashCode();
    }

    @Override
    public String toString() {
        return String.valueOf(this.getId());
    }

    public abstract List<String> validate();

    public List<String> getValidationResults() {
        return validationResults;
    }
}
