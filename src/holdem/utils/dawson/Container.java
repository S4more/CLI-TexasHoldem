package holdem.utils.dawson;

import holdem.graphic.dawson.Drawable;
import holdem.hud.dawson.VBoxContainer;

import java.util.ArrayList;
import java.util.Arrays;

public class Container {

    /**
     * Creates multiple VBoxContainers on the same position, split them as needed based on their max height.
     * @param x x coordinate of the VBoxes
     * @param y y coordinate of the VBoxes
     * @param width max width of the VBoxes
     * @param height max height of the VBoxes
     * @param elements the Drawable elements that are going to be put inside the container.
     * @return
     */
    public static VBoxContainer[] splitIntoMultiple(int x, int y, int width, int height, Drawable ... elements) {
        int VBoxIndex = 0;
        int VBoxActualHeight = 0;
        int elementsToAdd = 0;
        int lastAddedElement = 0;

        ArrayList<VBoxContainer> containers = new ArrayList<>();
        for (int i = 0; i < elements.length; i++) {
            elementsToAdd++;
            VBoxActualHeight += elements[i].getHeight();
            if (VBoxActualHeight + elements[i].getHeight() > height || i == elements.length - 1) {
                VBoxContainer vcontainer = new VBoxContainer(x, y, width, height,
                        Arrays.copyOfRange(elements, lastAddedElement, lastAddedElement + elementsToAdd));
                containers.add(vcontainer);
                lastAddedElement += elementsToAdd; // +1 so we don't add the same element twice.
                VBoxIndex++;
                VBoxActualHeight = 0;
                elementsToAdd = 0;
            }
        }

        VBoxContainer output[] = new VBoxContainer[containers.size()];

        // containers.forEach(e -> System.out.println(e));

        return containers.toArray(output);
    }

}
