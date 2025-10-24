package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;
import org.firstinspires.ftc.teamcode.subsystems.TurretSubsystem;
import java.util.function.BooleanSupplier;

/**
 * Manual turret control using the D-pad (left/right).
 * Right D-pad turns clockwise, left D-pad turns counterclockwise.
 */
public class TurretManualCommand extends CommandBase {

    private final TurretSubsystem turret;
    private final BooleanSupplier dpadLeft;
    private final BooleanSupplier dpadRight;

    // Manual turn speed (scale power)
    private static final double MANUAL_SPEED = 0.4;

    public TurretManualCommand(TurretSubsystem turret,
                               BooleanSupplier dpadLeft,
                               BooleanSupplier dpadRight) {
        this.turret = turret;
        this.dpadLeft = dpadLeft;
        this.dpadRight = dpadRight;
        addRequirements(turret);
    }

    @Override
    public void execute() {
        double power = 0;

        if (dpadLeft.getAsBoolean()) {
            power = -MANUAL_SPEED; // counterclockwise
        } else if (dpadRight.getAsBoolean()) {
            power = MANUAL_SPEED; // clockwise
        }

        if (Math.abs(power) > 0.05) {
            turret.setManualPower(power);
        } else {
            turret.holdPosition(); // hold when not pressed
        }
    }
}
