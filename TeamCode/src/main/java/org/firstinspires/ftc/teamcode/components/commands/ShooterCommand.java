package org.firstinspires.ftc.teamcode.components.commands;

import com.arcrobotics.ftclib.command.CommandBase;
import org.firstinspires.ftc.teamcode.components.subsystems.ShooterSubsystem;

/**
 * ShooterCommand controls the shooter flywheel using either:
 *  - target RPM (closed-loop velocity control), or
 *  - direct power (open-loop control)
 */
public class ShooterCommand extends CommandBase {

    private final ShooterSubsystem shooterSubsystem;
    private final double targetRPM;
    private final double shooterPower;
    private final boolean useRPMControl;

    /**
     * Constructor for velocity (RPM) control.
     *
     * @param subsystem the shooter subsystem
     * @param targetRPM the target RPM for velocity control
     */
    public ShooterCommand(ShooterSubsystem subsystem, double targetRPM) {
        shooterSubsystem = subsystem;
        this.targetRPM = targetRPM;
        this.shooterPower = 0;
        this.useRPMControl = true;

        addRequirements(subsystem);
    }

    /**
     * Constructor for open-loop (power) control.
     *
     * @param subsystem the shooter subsystem
     * @param shooterPower the motor power (-1.0 to 1.0)
     * @param useRPMControl pass false for power mode
     */
    public ShooterCommand(ShooterSubsystem subsystem, double shooterPower, boolean useRPMControl) {
        shooterSubsystem = subsystem;
        this.shooterPower = shooterPower;
        this.targetRPM = 0;
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
        // Optional: maintain or monitor shooter RPM here
        // e.g., log telemetry or dashboard data
    }

    @Override
    public void end(boolean interrupted) {
        shooterSubsystem.stopShooter();
    }

    @Override
    public boolean isFinished() {
        // Runs until interrupted
        return false;
    }
}
