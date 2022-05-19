package ru.otus.spring.task.manager.web.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;
import ru.otus.spring.task.manager.model.AttachmentEntity;
import ru.otus.spring.task.manager.web.dto.resp.TaskAttachmentDto;

@Component
@RequiredArgsConstructor
public class AttachEntityToDtoMapper extends AbstractConverter<AttachmentEntity, TaskAttachmentDto> {

    private final TaskUserEntityToDtoMapper taskUserEntityToDtoMapper;

    @Override
    protected TaskAttachmentDto convert(AttachmentEntity a) {
        return new TaskAttachmentDto(
                a.getId(),
                a.getUiFileName(),
                a.getDateOfCreation(),
                a.getStatus().name(),
                taskUserEntityToDtoMapper.convert(a.getOwner())
        );
    }
}
