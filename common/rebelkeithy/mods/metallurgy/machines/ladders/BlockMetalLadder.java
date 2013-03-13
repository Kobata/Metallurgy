package rebelkeithy.mods.metallurgy.machines.ladders;

import static net.minecraftforge.common.ForgeDirection.EAST;
import static net.minecraftforge.common.ForgeDirection.NORTH;
import static net.minecraftforge.common.ForgeDirection.SOUTH;
import static net.minecraftforge.common.ForgeDirection.WEST;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public class BlockMetalLadder extends Block
{
	public static int renderType = RenderingRegistry.getNextAvailableRenderId();
	private Icon[] icons;
	
    public BlockMetalLadder(int par1)
    {
        super(par1,Material.wood);
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
	@Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
	@Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    /**
     * The type of render function that is called for this block
     */
	@Override
    public int getRenderType()
    {
        return renderType;
    }

    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    @Override
    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
    {
        return par1World.isBlockSolidOnSide(par2 - 1, par3, par4, EAST ) ||
               par1World.isBlockSolidOnSide(par2 + 1, par3, par4, WEST ) ||
               par1World.isBlockSolidOnSide(par2, par3, par4 - 1, SOUTH) ||
               par1World.isBlockSolidOnSide(par2, par3, par4 + 1, NORTH);
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        int var5 = par1World.getBlockMetadata(par2, par3, par4);
        float var6 = 0.125F;

        if (var5 == 0)
        {
            this.setBlockBounds(0.0F, 0.0F, 1.0F - var6, 1.0F, 1.0F, 1.0F);
        }

        if (var5 == 1)
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, var6);
        }

        if (var5 == 2)
        {
            this.setBlockBounds(1.0F - var6, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }

        if (var5 == 3)
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, var6, 1.0F, 1.0F);
        }

        return super.getCollisionBoundingBoxFromPool(par1World, par2, par3, par4);
    }

    @SideOnly(Side.CLIENT)

    /**
     * Returns the bounding box of the wired rectangular prism to render.
     */
    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        int var5 = par1World.getBlockMetadata(par2, par3, par4) % 4;
        float var6 = 0.125F;

        if (var5 == 0)
        {
            this.setBlockBounds(0.0F, 0.0F, 1.0F - var6, 1.0F, 1.0F, 1.0F);
        }

        if (var5 == 1)
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, var6);
        }

        if (var5 == 2)
        {
            this.setBlockBounds(1.0F - var6, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }

        if (var5 == 3)
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, var6, 1.0F, 1.0F);
        }

        return super.getSelectedBoundingBoxFromPool(par1World, par2, par3, par4);
    }

    /**
     * called before onBlockPlacedBy by ItemBlock and ItemReed
     */
    @Override
    public int onBlockPlaced(World par1World, int par2, int par3, int par4, int par5, float par6, float par7, float par8, int par9)
    {
        int direction = par9 % 4;
        int type = par9 / 4;

        if ((false || par5 == 2) && par1World.isBlockSolidOnSide(par2, par3, par4 + 1, NORTH))
        {
            direction = 0;
        }

        else if ((false || par5 == 3) && par1World.isBlockSolidOnSide(par2, par3, par4 - 1, SOUTH))
        {
            direction = 1;
        }

        else if ((false || par5 == 4) && par1World.isBlockSolidOnSide(par2 + 1, par3, par4, WEST))
        {
            direction = 2;
        }

        else if ((false || par5 == 5) && par1World.isBlockSolidOnSide(par2 - 1, par3, par4, EAST))
        {
            direction = 3;
        }
        
        System.out.println("direction = " + direction);

        par1World.setBlockMetadataWithNotify(par2, par3, par4, type * 4 + direction, 2);
        return type * 4 + direction;
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    @Override
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        int type = par1World.getBlockMetadata(par2, par3, par4) / 4;
        int var6 = par1World.getBlockMetadata(par2, par3, par4) % 4;
        boolean canStay = false;

        if (var6 == 0 && par1World.isBlockSolidOnSide(par2, par3, par4 + 1, NORTH))
        {
            canStay = true;
        }

        if (var6 == 1 && par1World.isBlockSolidOnSide(par2, par3, par4 - 1, SOUTH))
        {
            canStay = true;
        }

        if (var6 == 2 && par1World.isBlockSolidOnSide(par2 + 1, par3, par4, WEST))
        {
            canStay = true;
        }

        if (var6 == 3 && par1World.isBlockSolidOnSide(par2 - 1, par3, par4, EAST))
        {
            canStay = true;
        }

        if (!canStay)
        {
            this.dropBlockAsItem(par1World, par2, par3, par4, type*4 + var6, 0);
            par1World.func_94571_i(par2, par3, par4); // Remove Block
        }

        super.onNeighborBlockChange(par1World, par2, par3, par4, par5);
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random par1Random)
    {
        return 1;
    }
    


    @Override
    public boolean isLadder(World world, int x, int y, int z)
    {
        return true;
    }
    
    @Override
	public int damageDropped(int metadata)
    {
    	return metadata/4;
    }
    
    public Icon getBlockTextureFromSideAndMetadata(int par1, int par2)
    {
    	return icons[par2];
    }

    /**
     * Triggered whenever an entity collides with this block (enters into the block). Args: world, x, y, z, entity
     */
    @Override
    public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity)
    {
    	int meta = par1World.getBlockMetadata(par2, par3, par4) / 4;
    	float movement = meta * 0.015F + 0.015F;
    	
    	if(par5Entity.motionY >= 0.1)
    		par5Entity.setPosition(par5Entity.posX, par5Entity.posY + movement, par5Entity.posZ);
    	//par5Entity.moveEntity(0, 0.1, 0);
    }
    
    @SideOnly(Side.CLIENT)
    public void func_94332_a(IconRegister par1IconRegister)
    {
		icons = new Icon[4];
    	for(int i = 0; i < 4; i++)
    	{
    		icons[i] = par1IconRegister.func_94245_a("Metallurgy:Ladder_" + i);
    	}
    }

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List) {
		for (int n = 0; n < 4; n++) {
			par3List.add(new ItemStack(this, 1, n));
		}
	}
}
