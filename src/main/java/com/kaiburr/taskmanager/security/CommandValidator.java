// CommandValidator.java
package com.kaiburr.taskmanager.security;

import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.List;

@Component
public class CommandValidator {
    
    private final List<String> dangerousCommands = Arrays.asList(
        "rm ", "del ", "format", "shutdown", "reboot", "mkfs", "dd ",
        "> /dev/", "chmod 777", "passwd", "useradd", "adduser",
        "wget", "curl", "nc ", "netcat", "ssh ", "scp ", "ftp ",
        "python", "perl", "ruby", "php", "node", "java -jar",
        "sudo", "su ", "kill", "pkill", "killall"
    );
    
    public boolean isSafeCommand(String command) {
        if (command == null || command.trim().isEmpty()) {
            return false;
        }
        
        String lowerCommand = command.toLowerCase().trim();
        
        // Check for dangerous commands
        for (String dangerous : dangerousCommands) {
            if (lowerCommand.contains(dangerous.toLowerCase())) {
                return false;
            }
        }
        
        // Allow only basic commands for demonstration
        List<String> allowedCommands = Arrays.asList(
            "echo", "ls", "pwd", "date", "whoami", "cat ", "grep ", 
            "find ", "wc ", "head ", "tail ", "sort ", "uniq "
        );
        
        boolean isAllowed = allowedCommands.stream()
            .anyMatch(allowed -> lowerCommand.startsWith(allowed));
            
        return isAllowed;
    }
}
