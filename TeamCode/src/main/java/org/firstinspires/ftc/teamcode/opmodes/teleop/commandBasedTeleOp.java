package org.firstinspires.ftc.teamcode.opmodes;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.subsystems.TurretSubsystem;
import org.firstinspires.ftc.teamcode.commands.TurretManualCommand;
import org.firstinspires.ftc.teamcode.commands.TurretSetAngleCommand;

@TeleOp(name = "Turret Command-Based TeleOp", group = "Command")
public class CommandBasedTeleOp extends CommandOpMode {

    private TurretSubsystem turret;

    // Preset angles
    private static final double PRESET_FRONT = 0.0;
    private static final double PRESET_LEFT = -90.0;
    private static final double PRESET_RIGHT = 90.0;
    private static final double PRESET_REAR = 180.0;

    @Override
    public void initialize() {
        // Initialize turret subsystem
        turret = new TurretSubsystem(hardwareMap, "turretMotor");

        // Default command: D-pad manual control
        turret.setDefaultCommand(
            new TurretManualCommand(
                turret,
                () -> gamepad1.dpad_left,   // turn counterclockwise
                () -> gamepad1.dpad_right   // turn clockwise
            )
        );

        telemetry.addLine("Turret Command-Based TeleOp Initialized");
    }

    @Override
    public void run() {
        super.run();

        // Preset buttons (A/B/X/Y)
        if (gamepad1.y) {
            schedule(new TurretSetAngleCommand(turret, PRESET_FRONT));
        } else if (gamepad1.x) {
            schedule(new TurretSetAngleCommand(turret, PRESET_LEFT));
        } else if (gamepad1.b) {
            schedule(new TurretSetAngleCommand(turret, PRESET_RIGHT));
        } else if (gamepad1.a) {
            schedule(new TurretSetAngleCommand(turret, PRESET_REAR));
        }

        // D-pad up/down incremental adjustments (small jogs)
        if (gamepad1.dpad_up) {
            turret.setTargetAngle(turret.getTargetAngle() + 5.0); // nudge clockwise
        } else if (gamepad1.dpad_down) {
            turret.setTargetAngle(turret.getTargetAngle() - 5.0); // nudge counterclockwise
        }

        // Start button used for zeroing encoder (only when turret physically at known front position)
        if (gamepad1.start) {
            turret.zeroEncoder();
            telemetry.addLine("Encoder zeroed at current position (0°).");
        }

        // Telemetry
        telemetry.addData("Current Angle (deg)", String.format("%.1f", turret.getCurrentAngle()));
        telemetry.addData("Target Angle (deg)", String.format("%.1f", turret.getTargetAngle()));
        telemetry.addData("On Target", Math.abs(turret.getCurrentAngle() - turret.getTargetAngle()) < 1.0);
        telemetry.update();
    }
}
