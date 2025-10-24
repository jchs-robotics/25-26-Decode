package org.firstinspires.ftc.teamcode.components.commands;

import com.arcrobotics.ftclib.command.CommandBase;
import org.firstinspires.ftc.teamcode.components.subsystems.IntakeSubsystem;

public class IntakeCommand extends CommandBase {
    private final IntakeSubsystem m_subsystem;

    public IntakeCommand(IntakeSubsystem subsystem, double intakePower) {
        m_subsystem = subsystem;
        m_subsystem.setIntake(intakePower);
        addRequirements(subsystem);
    }

    @Override
    public void initialize() {
        // Optional: could set intake power here instead of constructor
    }

    @Override
    public void execute() {
        // This command simply runs the intake at the given power
    }

    @Override
    public void end(boolean interrupted) {
        // Stop the intake when the command ends
        m_subsystem.setIntake(0);
    }

    @Override
    public boolean isFinished() {
        // Keep running until interrupted
        return false;
    }
}

