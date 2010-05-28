/**

   Copyright 2010 OpenEngSB Division, Viennimport java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openengsb.ekb.api.Concept;
import org.openengsb.ekb.api.ConceptSource;
import org.openengsb.ekb.api.EKB;
import org.openengsb.ekb.api.ReferenceableConcept;
import org.openengsb.ekb.api.SoftReference;
se is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package org.openengsb.ekb.core.softreferences;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openengsb.ekb.api.Concept;
import org.openengsb.ekb.api.ConceptSource;
import org.openengsb.ekb.api.EKB;
import org.openengsb.ekb.api.ReferenceableConcept;
import org.openengsb.ekb.api.SoftReference;

public class RegexSoftReference<SOURCETYPE, TARGETTYPE> implements SoftReference<SOURCETYPE, TARGETTYPE> {

    private Concept<SOURCETYPE> sourceConcept;

    private ReferenceableConcept<TARGETTYPE> targetConcept;

    private String referenceField;

    @Override
    public List<TARGETTYPE> follow(EKB ekb, SOURCETYPE sourceObject) {
        try {
            Class<SOURCETYPE> sourceClass = sourceConcept.getConceptClass();
            Object sourceFieldValue = getReferenceFieldValue(sourceObject, sourceClass);
            String sourceFieldText = String.valueOf(sourceFieldValue);
            return findAndFollow(ekb, sourceFieldText);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object getReferenceFieldValue(SOURCETYPE sourceObject, Class<SOURCETYPE> sourceClass)
            throws NoSuchFieldException, IllegalAccessException {
        Field sourceField = sourceClass.getDeclaredField(referenceField);
        boolean accessible = sourceField.isAccessible();
        sourceField.setAccessible(true);
        Object sourceFieldValue = sourceField.get(sourceObject);
        sourceField.setAccessible(accessible);
        return sourceFieldValue;
    }

    private List<TARGETTYPE> findAndFollow(EKB ekb, String sourceFieldText) {
        Matcher matcher = Pattern.compile(targetConcept.getReferenceRegex()).matcher(sourceFieldText);

        List<TARGETTYPE> result = new ArrayList<TARGETTYPE>();
        while (matcher.find()) {
            String matchingPart = matcher.group();

            String key = targetConcept.extractKey(matchingPart);
            if (key != null) {
                TARGETTYPE element = getTargetElementByKey(ekb, key);
                if (element != null) {
                    result.add(element);
                }
            }

        }
        return result;
    }

    private TARGETTYPE getTargetElementByKey(EKB ekb, String key) {
        List<ConceptSource> sources = ekb.getSources(targetConcept);
        for (ConceptSource source : sources) {
            TARGETTYPE result = ekb.getDataElement(source, targetConcept, key);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    @Override
    public Concept<SOURCETYPE> getSourceConcept() {
        return sourceConcept;
    }

    @Override
    public Concept<TARGETTYPE> getTargetConcept() {
        return targetConcept;
    }

    public void setReferenceField(String referenceField) {
        this.referenceField = referenceField;
    }

    public String getReferenceField() {
        return referenceField;
    }

}
