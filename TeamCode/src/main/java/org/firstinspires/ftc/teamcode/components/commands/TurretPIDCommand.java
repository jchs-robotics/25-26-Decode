package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;
import org.firstinspires.ftc.teamcode.subsystems.TurretSubsystem;

/**
 * TurretPIDCommand
 *
 * Moves the turret to a specified angle using the PID controller
 * inside the TurretSubsystem.
 *
 * Example usage:
 *   schedule(new TurretPIDCommand(turret, 90.0)); // turn 90 degrees right
 */
public class TurretPIDCommand extends CommandBase {

    private final TurretSubsystem turret;
    private final double targetAngle;
    private final double toleranceDeg;

    /**
     * Creates a new TurretPIDCommand.
     *
     * @param turret       the turret subsystem
     * @param targetAngle  target angle in degrees
     * @param toleranceDeg allowed error before finishing (default ~1.0 deg)
     */
    public TurretPIDCommand(TurretSubsystem turret, double targetAngle, double toleranceDeg) {
        this.turret = turret;
        this.targetAngle = targetAngle;
        this.toleranceDeg = toleranceDeg;
        addRequirements(turret);
    }

    public TurretPIDCommand(TurretSubsystem turret, double targetAngle) {
        this(turret, targetAngle, 1.0);
    }

    @Override
    public void initialize() {
        turret.setTargetAngle(targetAngle);
    }

    @Override
    public void execute() {
        // Nothing else needed — turret PID runs in subsystem.periodic()
    }

    @Override
    public boolean isFinished() {
        double current = turret.getCurrentAngle();
        return Math.abs(current - targetAngle) <= toleranceDeg;
    }

    @Override
    public void end(boolean interrupted) {
        if (interrupted) {
            turret.holdPosition(); // stop and hold if interrupted
        } else {
            turret.holdPosition(); // hold target once reached
        }
    }
}
