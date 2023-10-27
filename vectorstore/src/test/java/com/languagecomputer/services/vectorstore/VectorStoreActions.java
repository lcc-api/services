package com.languagecomputer.services.vectorstore;

import com.google.common.base.Preconditions;
import com.languagecomputer.services.client.sample.LCCServiceInfo;
import com.languagecomputer.services.client.sample.SampleOutput;
import com.languagecomputer.services.client.util.CommandLineUtils;
import com.languagecomputer.services.util.JacksonUtil;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;

public class VectorStoreActions {

  enum VectorStoreAction {
    ADD_VECTOR,
    GET_VECTOR_BY_ID_SPACE,
    FIND_NN
  }

  private static class Arguments extends CommandLineUtils.ServiceArgs {

    @CommandLine.Option(names = {"--action"}, description = {"the action to take"}, required=true)
    VectorStoreAction action;

    @CommandLine.Option(names = {"--id"}, description = {"the id of the vector"}, required=false)
    String id = null;
    @CommandLine.Option(names = {"--space"}, description = {"the space to work with"}, required=true)
    String space;
    @CommandLine.Option(names = {"--vector"}, description = {"the vector to work with"}, required=false)
    String vector = null;

    @CommandLine.Option(names = {"--nn"}, description = {"the number of nearest neighbors to find"}, required=false)
    Integer numNeighbors = 10;
  }

  public static void main(String[] rawArgs) throws IOException {
    Arguments args = CommandLineUtils.parseArgs(new Arguments(), rawArgs);
    VectorStoreService vs = new LCCServiceInfo(args).getService("VECTOR_STORE", VectorStoreService.class);
    switch(args.action) {
      case ADD_VECTOR -> addVector(vs, args.space, args.id, args.vector);
      case GET_VECTOR_BY_ID_SPACE -> getVector(vs, args.space, args.id);
      case FIND_NN -> findNN(vs, args.space, args.vector, args.numNeighbors);
    }
  }

  private static void findNN(VectorStoreService vs, String space, String vectorString, Integer nn) {
    Preconditions.checkNotNull(space);
    Preconditions.checkNotNull(vectorString);
    double[] vector = parseVectorString(vectorString);
    final VectorNearestNeighborsQuery query = new VectorNearestNeighborsQuery(vector, nn, null);
    final VectorNeighbors nn1 = vs.findNN(space, query);
    for (int i = 0; i < nn1.getNeighbors().size(); i++) {
      SampleOutput.printF("%d nearest neighbor is %s with distance %f", i, nn1.getNeighbors().get(i).getId(), nn1.getNeighbors().get(i).getDistance());
    }
  }

  private static void getVector(VectorStoreService vs, String space, String id) {
    Preconditions.checkNotNull(space);
    Preconditions.checkNotNull(id);
    final VectorMessage vector = vs.getVector(space, id);
    SampleOutput.println(vector);
  }

  public static void addVector(VectorStoreService vs, String space, String id, String vectorString) {
    Preconditions.checkNotNull(space);
    Preconditions.checkNotNull(id);
    Preconditions.checkNotNull(vectorString);
    double[] vector = parseVectorString(vectorString);
    final VectorMessage vectorMessage = new VectorMessage(id, vector);
    SampleOutput.println(vectorMessage);
    vs.putVector(space, vectorMessage);
  }

  private static double[] parseVectorString(String vectorString) {
    final String[] split = vectorString.split(",");
    double[] array = new double[split.length];
    for (int i = 0; i < split.length; i++) {
      array[i] = Double.valueOf(split[i]);
    }
    return array;
  }
}