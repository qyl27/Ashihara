package kogasastudio.ashihara.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ShapeHelper
{
    public static VoxelShape getWallShape(Direction direction, Direction.Axis axis)
    {
        if (direction.equals(Direction.NORTH)) return Block.box(7, 0, 0, 9, 16, 1);
        if (direction.equals(Direction.WEST)) return Block.box(0, 0, 7, 1, 16, 9);
        if (direction.equals(Direction.SOUTH)) return Block.box(7, 0, 15, 9, 16, 16);
        if (direction.equals(Direction.EAST)) Block.box(15, 0, 7, 16, 16, 9);
        if (direction.equals(Direction.UP))
        {
            if (axis.equals(Direction.Axis.X)) return Block.box(0, 15, 7, 16, 16, 9);
            else return Block.box(7, 15, 0, 9, 16, 16);
        }
        else
        {
            if (axis.equals(Direction.Axis.X)) return Block.box(0, 0, 7, 16, 1, 9);
            else return Block.box(7, 0, 0, 9, 1, 16);
        }
    }

    /**
     * 检测某方块是否在某方向的形状是否符合某种形状要求。
     * 判断标准：获取该方块在该方向的形状，尝试按照某种规则与需求的形状相交。若相交后的形状不为空，返回true，否则返回false
     * @param shapeNeeded 需对比的形状
     */
    public static boolean canBlockSupport(BlockState supporter, BlockGetter getter, BlockPos supportedPos, Direction direction, VoxelShape shapeNeeded)
    {
        VoxelShape shape = supporter.getBlockSupportShape(getter, supportedPos);
        VoxelShape face = shape.getFaceShape(direction);
        //VoxelShape merged = Shapes.or(face, shapeNeeded);
        //boolean f = merged.equals(shapeNeeded);
        return !Shapes.joinIsNotEmpty(face, shapeNeeded, BooleanOp.ONLY_SECOND);
    }

    public static VoxelShape rotateShape(VoxelShape shape, double rotation)
    {
        return rotateShape(shape, Direction.Axis.Y, rotation);
    }

    public static VoxelShape rotateShape(VoxelShape shape, Direction.Axis axis, double rotation)
    {
        return rotateShape(shape, axis, 0.5, 0.5, rotation);
    }

    public static VoxelShape rotateShape(VoxelShape shape, Direction.Axis axis, double pivotX, double pivotZ, double rotation)
    {
        double degree = Math.toRadians(rotation);
        VoxelShape[] rotatedShape = new VoxelShape[1];
        shape.forAllBoxes((x1, y1, z1, x2, y2, z2) ->
                          {
                              double rx1, ry1, rz1, rx2, ry2, rz2;
                              switch (axis)
                              {
                                  case Y ->
                                  {
                                      rx1 = x1;
                                      rx2 = x2;
                                      rz1 = z1;
                                      rz2 = z2;
                                      ry1 = y1;
                                      ry2 = y2;
                                  }
                                  case X ->
                                  {
                                      rx1 = z1;
                                      rx2 = z2;
                                      rz1 = y1;
                                      rz2 = y2;
                                      ry1 = x1;
                                      ry2 = x2;
                                  }
                                  default ->
                                  {
                                      rx1 = x1;
                                      rx2 = x2;
                                      rz1 = y1;
                                      rz2 = y2;
                                      ry1 = z1;
                                      ry2 = z2;
                                  }
                              }
                              double[] rotatedP1 = MathHelper.rotatePoint(rx1, rz1, pivotX, pivotZ, degree);
                              double[] rotatedP2 = MathHelper.rotatePoint(rx2, rz2, pivotX, pivotZ, degree);
                              VoxelShape s = Shapes.box(rotatedP1[0], ry1, rotatedP1[1], rotatedP2[0], ry2, rotatedP2[1]);
                              rotatedShape[0] = s;
                          });
        return rotatedShape[0];
    }
}
