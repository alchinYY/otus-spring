package ru.otus.spring.library.util;

import com.mongodb.DBRef;
import lombok.experimental.UtilityClass;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.otus.spring.library.domain.AbstractDomain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@UtilityClass
public class ChangelogUtil {

    public static String getCollectionName(Class<? extends AbstractDomain> aClass){
        return aClass.getAnnotation(Document.class).value();
    }
    public static DBRef createDbRef(String collection, Integer id){
        return new DBRef(collection, id);
    }

    public static List<DBRef> createReferenceList(List<Integer> refersIdList, Class<? extends AbstractDomain> aClass) {
        if(Objects.nonNull(refersIdList)) {
            List<DBRef> list = new ArrayList<>();
            String collection = ChangelogUtil.getCollectionName(aClass);
            refersIdList.forEach(id -> list.add(createDbRef(collection, id)));
            return list;
        } else {
            return List.of();
        }
    }
}
