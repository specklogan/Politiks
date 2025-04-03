package org.gooseapple.politiks.core.land.types;

import org.gooseapple.politiks.core.land.AbstractLand;
import org.gooseapple.politiks.core.land.geometry.IClaim;
import org.gooseapple.politiks.util.Constants;

public class Land extends AbstractLand {

    public Land(Constants.LandType type, IClaim claim) {
        super(type, claim);
    }

    public Land() {
        super();
    }
}
