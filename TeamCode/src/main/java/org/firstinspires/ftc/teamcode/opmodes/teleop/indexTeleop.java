package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.CommandScheduler;

import org.firstinspires.ftc.teamcode.components.subsystems.IndexSubsystem;
import org.firstinspires.ftc.teamcode.components.commands.IndexCommand;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Index TeleOp (X/B)", group = "Competition")
public class IndexTeleOp extends CommandOpMode {

    private IndexSubsystem indexSubsystem;
    private IndexCommand indexForwardCommand;
    private IndexCommand indexReverseCommand;

    private boolean indexRunningForward = false;
    private boolean indexRunningReverse = false;

    @Override
    public void initialize() {
        telemetry.addLine("Initializing Index TeleOp...");
        telemetry.update();

        // Initialize subsystem
        indexSubsystem = new IndexSubsystem(hardwareMap, "indexMotor");

        // Prepare commands
        indexForwardCommand = new IndexCommand(indexSubsystem, 1.0);
        indexReverseCommand = new IndexCommand(indexSubsystem, -1.0);

        telemetry.addLine("Index TeleOp Initialized!");
        telemetry.update();
    }

    @Override
    public void run() {
        super.run(); // Required for FTCLib CommandScheduler

        // --- Control Indexer ---
        if (gamepad2.y && !indexRunningForward) {
            // Run indexer forward
            CommandScheduler.getInstance().schedule(indexForwardCommand);
            indexRunningForward = true;
            indexRunningReverse = false;
        } 
        else if (gamepad2.a && !indexRunningReverse) {
            // Run indexer backward
            CommandScheduler.getInstance().schedule(indexReverseCommand);
            indexRunningReverse = true;
            indexRunningForward = false;
        } 
        else if (!gamepad2.y && !gamepad2.a) {
            // Stop indexer when neither button is pressed
            CommandScheduler.getInstance().cancelAll();
            indexSubsystem.stopIndex();
            indexRunningForward = false;
            indexRunningReverse = false;
        }

        // --- Telemetry ---
        telemetry.addLine("==== Indexer ====");
        telemetry.addData("Power", indexSubsystem.getIndexPower());
        telemetry.addData("Forward Running (Y)", indexRunningForward);
        telemetry.addData("Reverse Running (A)", indexRunningReverse);
        telemetry.update();
    }
}
