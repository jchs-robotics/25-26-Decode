package org.firstinspires.ftc.teamcode.components.commands;

import com.arcrobotics.ftclib.command.CommandBase;
import org.firstinspires.ftc.teamcode.components.subsystems.ShooterSubsystem;

public class ShooterCommand extends CommandBase {

    private final ShooterSubsystem shooterSubsystem;
    private final double targetRPM;
    private final double shooterPower;
    private final boolean useRPMControl;

    // Closed-loop RPM control
    public ShooterCommand(ShooterSubsystem subsystem, double targetRPM) {
        shooterSubsystem = subsystem;
        this.targetRPM = targetRPM;
        this.shooterPower = 0.99;
        this.useRPMControl = true;

        addRequirements(subsystem);
    }

    // Open-loop power control
    public ShooterCommand(ShooterSubsystem subsystem, double shooterPower, boolean useRPMControl) {
        shooterSubsystem = subsystem;
        this.shooterPower = shooterPower;
        this.targetRPM = 1620; // default nominal RPM for this motor
        this.useRPMControl = useRPMControl;

        addRequirements(subsystem);
    }

    @Override
    public void initialize() {
        if (useRPMControl) {
            shooterSubsystem.setShooterRPM(targetRPM);
        } else {
            shooterSubsystem.setShooterPower(shooterPower);
        }
    }

    @Override
    public void execute() {
        double currentRPM = shooterSubsystem.getCurrentRPM();
        System.out.println("Shooter RPM: " + currentRPM);
    }

    @Override
    public void end(boolean interrupted) {
        shooterSubsystem.stopShooter();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
