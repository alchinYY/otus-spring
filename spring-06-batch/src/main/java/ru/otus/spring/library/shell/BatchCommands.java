package ru.otus.spring.library.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.library.job.MigrationJob;

@RequiredArgsConstructor
@ShellComponent
public class BatchCommands {

    private final MigrationJob migrationJob;

    @ShellMethod(value = "start migration to mongo", key = "h2-m")
    public void startMigration() {
        migrationJob.migrate();
    }

}
