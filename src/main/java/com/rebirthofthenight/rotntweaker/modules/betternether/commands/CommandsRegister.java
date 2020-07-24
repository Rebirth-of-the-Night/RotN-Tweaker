package com.rebirthofthenight.rotntweaker.modules.betternether.commands;

import net.minecraft.command.ServerCommandManager;

public class CommandsRegister
{
	public static void register(ServerCommandManager manager)
	{
		manager.registerCommand(new CommandFindCity());
	}
}
