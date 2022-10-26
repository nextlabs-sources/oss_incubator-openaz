/*
 * Created on Jan 19, 2022
 *
 * All sources, binaries and HTML pages (C) copyright 2022 by NextLabs Inc.,
 * San Mateo CA, Ownership remains with NextLabs Inc, All rights reserved
 * worldwide.
 *
 * @author amorgan
 */

package org.apache.openaz.pepapi.std;

import org.apache.openaz.pepapi.PepPermissionsResponse;
import org.apache.openaz.pepapi.PepPermissionsResponseFactory;
import org.apache.openaz.xacml.api.PermissionsResult;

final class StdPepPermissionsResponseFactory implements PepPermissionsResponseFactory {
    StdPepPermissionsResponseFactory() {
    }

    @Override
    public PepPermissionsResponse newPepPermissionsResponse(PermissionsResult result) {
        return StdPepPermissionsResponse.newInstance(result);
    }
}
