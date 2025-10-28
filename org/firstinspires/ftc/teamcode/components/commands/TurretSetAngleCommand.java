package org.firstinspires.ftc.teamcode.components.commands;

import com.arcrobotics.ftclib.command.CommandBase;
import org.firstinspires.ftc.teamcode.components.subsystems.TurretSubsystem;

public class TurretSetAngleCommand extends CommandBase {

    private final TurretSubsystem turret;
    private final double targetAngle;
    private final double toleranceDeg = 1.0;

    public TurretSetAngleCommand(TurretSubsystem turret, double angleDeg) {
        this.turret = turret;
        this.targetAngle = angleDeg;
        addRequirements(turret);
    }

    @Override
    public void initialize() {
        turret.setTargetAngle(targetAngle);
    }

    @Override
    public boolean isFinished() {
        return Math.abs(turret.getCurrentAngle() - targetAngle) < toleranceDeg;
    }
}
