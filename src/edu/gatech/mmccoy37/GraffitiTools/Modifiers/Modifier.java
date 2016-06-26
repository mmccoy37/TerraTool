package edu.gatech.mmccoy37.GraffitiTools.Modifiers;

import edu.gatech.mmccoy37.GraffitiTools.Data.BlockData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by matt on 6/26/16.
 */
public abstract class Modifier {

    public abstract HashMap<Location, BlockData>
        modifyTargetSet(HashMap<Location, BlockData> blocks, BlockData bdNew);

}
