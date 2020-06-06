package CMD;

import CMD.Interfaces.Helpable;
import CMD.Interfaces.Runnable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static Path workingDir = Paths.get("").toAbsolutePath();

    public static void run() {
        Scanner in = new Scanner(System.in);
        String commandLine;
        while ((commandLine = in.nextLine()).compareTo("exit") != 0) {
            if (commandLine.length() > 0) {
                int i = 0;
                boolean wasChar = false;
                while (i < commandLine.length() && (wasChar == false || commandLine.charAt(i) != ' ')) {
                    if (commandLine.charAt(i) != ' ')
                        wasChar = true;
                    i++;
                }
                String command = commandLine.substring(0, i).toLowerCase();
                command = command.substring(0, 1).toUpperCase() + command.substring(1, i);
                String parameters;
                try {
                    parameters = commandLine.substring(i + 1, commandLine.length());
                }
                catch (Exception e) {
                    parameters = "";
                }
                try {
                    Class commandClass = Class.forName("CMD.Commands." + command);
                    Constructor construct = commandClass.getConstructor();
                    Object obj = construct.newInstance();
                    Runnable mobj = (Runnable) obj;
                    i = 0;
                    while (i < parameters.length() && parameters.charAt(i) == ' ')
                        i++;
                    parameters = parameters.substring(i, parameters.length());
                    i = parameters.length() - 1;
                    while (i >= 0 && parameters.charAt(i) == ' ') {
                        i--;
                    }
                    parameters = parameters.substring(0, ++i);
                    if (parameters.equals(""))
                        mobj.run();
                    else {
                        try {
                            mobj.run(parameters);
                        }
                        catch (Exception ex) {
                            System.out.println(ex);
                        }
                    }
                } catch (ClassNotFoundException e) {
                    System.out.println("No such command");
                    try {
                        List<Class<?>> classes = Finder.find("CMD.Commands");
                        for (Class<?> commandClass:classes) {
                            commandClass = Class.forName(commandClass.toString().substring(6, commandClass.toString().length()));
                            if (!commandClass.toString().contains("$")) {
                                Constructor construct = commandClass.getConstructor();
                                Object obj = construct.newInstance();
                                Helpable mobj = (Helpable) obj;
                                mobj.help();
                            }
                        }
                    } catch (NoSuchMethodException ex) {
                        ex.printStackTrace();
                    } catch (IllegalAccessException ex) {
                        ex.printStackTrace();
                    } catch (InvocationTargetException ex) {
                        ex.printStackTrace();
                    } catch (ClassNotFoundException ex) {
                        ex.printStackTrace();
                    } catch (InstantiationException ex) {
                        ex.printStackTrace();
                    }

                } catch (NoSuchMethodException e) {
                    System.out.println("Programmer did not implement run. My condolences.");
                } catch (IllegalAccessException e) {
                    System.out.println(e + "\n" + "Programmer did mistake. My condolences.");
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void main(String[] args) {
        if (args.length > 0) {
            workingDir = Paths.get(args[0]).toAbsolutePath().normalize();
        }
        run();
    }
}
