package edu.gatech.mmccoy37.GraffitiTools.Commands;

import edu.gatech.mmccoy37.GraffitiTools.Brushes.Brush;
import edu.gatech.mmccoy37.GraffitiTools.Data.BlockData;
import edu.gatech.mmccoy37.GraffitiTools.Data.PlayerStates;
import edu.gatech.mmccoy37.GraffitiTools.Tools.Tool;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import static org.bukkit.Material.STICK;

/**
 * Created by matt on 5/14/16.
 */
public class CommandParse implements CommandExecutor {

    private static final List<String> COLORS = Arrays.asList(
            "RED", "ORANGE", "YELLOW", "GREEN", "BLUE", "PURPLE", "WHITE", "BLACK", "GRAY"
    );
    private static final String TAG = (ChatColor.AQUA + "Graffiti Tools" + ChatColor.YELLOW + ":" + ChatColor.RESET);
    private static final String TAB = "    ";
    private static final List<String> DIST_ALIAS = Arrays.asList(
            "DIST", "DISTANCE", "D"
    );

    @Override
    public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args) {
        if (sender instanceof Player) {
            Player p = ((Player)sender);


            //STATE: ENABLED
            if (PlayerStates.hasPlayer(p)) {
                Log.info(PlayerStates.getString());
                Tool tool = PlayerStates.getData(p).getTool();
                Brush brush = tool.getBrush();




                if (args.length == 0) {
                    //status
                    String material = tool.getBlockNew().toString();
                    p.sendMessage(TAG + ChatColor.ITALIC + " Status");
                    p.sendMessage(TAB + " color:    " + ChatColor.YELLOW + material);
                    p.sendMessage(TAB + " size:      " + ChatColor.YELLOW + brush.getSize());
                    p.sendMessage(TAB + " dist:       " + ChatColor.YELLOW + tool.getDistance());
                    p.sendMessage(TAB + " enabled: " + ChatColor.YELLOW + PlayerStates.hasPlayer(p));



                } else if (args.length == 1) {
                    //disable
                    if (args[0].equalsIgnoreCase("on")) {
                        PlayerStates.add(p);
                        p.sendMessage(TAG + " spraying toggled " + ChatColor.GREEN + "on.");
                    } else if (args[0].equalsIgnoreCase("off")) {
                        PlayerStates.remove(p);
                        p.sendMessage(TAG + " spraying toggled " + ChatColor.RED + "off.");
                    } else if (args[0].equalsIgnoreCase("can")) {
                        //give wand
                        if (p.getInventory().getItemInMainHand().getTypeId() == 0) {
                            p.getInventory().setItemInMainHand(new ItemStack(STICK, 1));
                        } else {
                            p.sendMessage(TAG + " please empty your hand first!");
                        }
                    } else {
                        //change color
                        args[0] = args[0].toUpperCase();
                        if (COLORS.contains(args[0])) {
                            //Wands.setColor(p, args[0]);
                            p.sendMessage(TAG + " color set to: " + args[0]);
                        } else {
                            p.sendMessage(TAG + " Please use one of the following colors:\n"
                                    + COLORS.toString().replace("[", "").replace("]", ""));
                        }
                    }



                } else if (args.length == 2) {
                    //distance
                    if (args[0].equalsIgnoreCase("dist")) {
                        try {
                            int num = Integer.parseInt(args[1]);
                            if (num < 100 && num > 0) {
                                tool.setDistance(num);
                            } else {
                                throw new NumberFormatException();
                            }
                        } catch (NumberFormatException e) {
                            p.sendMessage(TAG + " enter a numer from 1 to 100.");
                            return false;
                        }
                        p.sendMessage(TAG + " ditance set to " + ChatColor.YELLOW + args[1]);
                    } else if (args[0].equalsIgnoreCase("size")) {
                        //size
                        try {
                            int num = Integer.parseInt(args[1]);
                            if (num < brush.MAX_SIZE && num > brush.MIN_SIZE) {
                                brush.setSize(num);
                            } else {
                                throw new NumberFormatException();
                            }
                        } catch (NumberFormatException e) {
                            p.sendMessage(TAG + " enter a numer from " + brush.MIN_SIZE + " to " + brush.MAX_SIZE);
                            return false;
                        }
                        p.sendMessage(TAG + " size set to " + ChatColor.YELLOW + args[1]);
                    } else if (args[0].equalsIgnoreCase("undo")) {
                        //undo
                        try {
                            int num = Integer.parseInt(args[1]);
                            if (num < 100 && num > 0) {
                                undo(p, num);
                            } else {
                                throw new NumberFormatException();
                            }
                        } catch (NumberFormatException e) {
                            p.sendMessage(TAG + " enter a number from 1 to 10.");
                            return false;
                        }
                        p.sendMessage(TAG + " performed undo for past actions " + ChatColor.YELLOW + args[1]);

                    } else {
                        //spelling error
                        p.sendMessage(TAG + " unknown action. Check spelling.");
                    }
                } else if (args.length > 2) {
                    //ARGS ERROR
                    p.sendMessage(TAG + " unknown action. too many arguments.");
                }






                //STATE: DISABLED
            } else {
                //status
                if (args.length == 0) {
                    p.sendMessage(TAG + ChatColor.ITALIC + " Status");
                    p.sendMessage(TAB + " enabled: " + ChatColor.YELLOW + PlayerStates.hasPlayer(p));


                    //enable
                } else if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("on")) {
                        PlayerStates.add(p);
                        p.sendMessage(TAG + " spraying toggled " + ChatColor.GREEN + "on.");
                    } else if (args[0].equalsIgnoreCase("off")) {
                        PlayerStates.remove(p);
                        p.sendMessage(TAG + " spraying toggled " + ChatColor.RED + "off.");
                    } else {
                        p.sendMessage(TAG + " your can must be " + ChatColor.GREEN + "enabled" + ChatColor.RESET + " to do that.");
                    }


                } else {
                    //error msg
                    p.sendMessage(TAG + " invalid arguments length.");
                }
            }
        }
        return true;
    }
    private void undo(Player p, int count) {
        Stack<HashMap<Location, BlockData>> temp =
                PlayerStates.getData(p).changes;
        for (int i = 0; i < count && !temp.isEmpty(); i++) {
            HashMap<Location, BlockData> change = temp.pop();
            for (Location loc: change.keySet()) {
                if (loc != null) {
                    Block block = loc.getBlock();
                    block.setType(change.get(loc).getMaterial());
                    block.setData(change.get(loc).getData());
                }

            }
        }
    }

}
