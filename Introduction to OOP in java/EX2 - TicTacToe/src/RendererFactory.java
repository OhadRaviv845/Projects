public class RendererFactory {

    /**
     * The renderer factory sets weather to display the board in each game (using console renderer) or not (using void renderer).
     * @param renderer - the parameter which is taken from the command that activates the program.
     * @return
     */
    public Renderer buildRenderer(String renderer) {
        switch (renderer) {
            case "console":
                return new ConsoleRenderer();
            case "none":
                return new VoidRenderer();
            default:
                return null;
        }
    }
}
