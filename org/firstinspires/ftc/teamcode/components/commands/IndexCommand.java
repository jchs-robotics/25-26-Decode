package org.firstinspires.ftc.teamcode.components.commands;

import com.arcrobotics.ftclib.command.CommandBase;
import org.firstinspires.ftc.teamcode.components.subsystems.IndexSubsystem;

public class IndexCommand extends CommandBase {

    private final IndexSubsystem indexSubsystem;
    private final double indexPower;

    public IndexCommand(IndexSubsystem subsystem, double power) {
        indexSubsystem = subsystem;
        indexPower = power;
        addRequirements(subsystem);
    }

    @Override
    public void initialize() {
        indexSubsystem.setIndexPower(indexPower);
    }

    @Override
    public void execute() {
        // could add timing or sensor-based logic here
    }

    @Override
    public void end(boolean interrupted) {
        indexSubsystem.stopIndex();
    }

    @Override
    public boolean isFinished() {
        // Keep running until interrupted
        return false;
    }
}
