package com.lucasmafra.socialnetworking.infrastructure.console.handlers;

import com.lucasmafra.socialnetworking.domain.services.WallService;
import com.lucasmafra.socialnetworking.domain.services.WallServiceImpl;
import com.lucasmafra.socialnetworking.domain.usecases.readwall.*;
import com.lucasmafra.socialnetworking.infrastructure.console.main.AppContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadWallHandler extends BaseHandler {

    private AppContext context;
    private ReadWallOutputBoundary presenter;
    private ReadWallView view;

    public ReadWallHandler(AppContext context, ReadWallOutputBoundary presenter, ReadWallView view) {
        this.context = context;
        this.presenter = presenter;
        this.view = view;
    }

    @Override
    public void handle(String input) {
        WallService wallService = createWallService();
        ReadWallInputBoundary useCase = new ReadWallInteractor(wallService);
        ReadWallController controller = new ReadWallController(parseInput(input), useCase, presenter, view);
        controller.control();
    }

    @Override
    Pattern getInputPattern() {
        return context.getCommands().getReadWallCommandPattern();
    }

    private ReadWallRequestModel parseInput(String input) {
        Matcher matcher = getInputPattern().matcher(input);
        matcher.matches();
        String user = matcher.group(1);
        return new ReadWallRequestModel(user);
    }

    private WallServiceImpl createWallService() {
        return new WallServiceImpl(context.getPostGateway(), context.getFollowGateway());
    }
}
