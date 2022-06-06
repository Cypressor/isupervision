package com.cypress.isupervision.data;

import com.cypress.isupervision.data.entity.user.Assistant;
import com.cypress.isupervision.data.entity.user.Student;

import java.util.Comparator;

public class AssistantComparator implements Comparator<Assistant>
{
    public int compare(Assistant assistant1, Assistant assistant2)
    {
        String assistantName1=assistant1.getFirstname().toUpperCase();
        String assistantName2=assistant2.getFirstname().toUpperCase();

        return assistantName1.compareTo(assistantName2);
    }
}