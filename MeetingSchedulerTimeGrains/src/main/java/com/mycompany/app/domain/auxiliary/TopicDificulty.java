package com.mycompany.app.domain.auxiliary;

import com.mycompany.app.domain.*;
import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;

public class TopicDificulty implements Comparator<Topic> {
    public int compare(Topic t1, Topic t2) {
        return new CompareToBuilder()
                .append(t1.getTopicPersonListSize(), t2.getTopicPersonListSize())
                .append(t1.getId(), t2.getId())
                .toComparison();
    }
}
