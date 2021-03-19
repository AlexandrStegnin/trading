package com.ddkolesnik.trading.vaadin.support;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Objects;

@Component
public class VaadinViewUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(VaadinViewUtils.class);

    public static Div makeEditorColumnActions(ComponentEventListener<ClickEvent<Button>> editListener,
                                              ComponentEventListener<ClickEvent<Button>> deleteListener) {
        Div actions = new Div();
        Button edit = new Button();
        Html editIcon = new Html("<i class=\"material-icons\">mode_edit</i>");
        edit.addClassNames("btn", "btn-xs", "bg-blue");
        edit.getStyle().set("margin-right", "5px");
        edit.setIcon(editIcon);
        edit.addClickListener(editListener);
        Button delete = new Button();
        Html deleteIcon = new Html("<i class=\"material-icons\">delete</i>");
        delete.addClassNames("btn", "btn-xs", "bg-red");
        delete.getStyle().set("margin-left", "5px");
        delete.setIcon(deleteIcon);
        delete.addClickListener(deleteListener);
        actions.add(edit, delete);
        return actions;
    }

    public static Div makeEditColumnAction(ComponentEventListener<ClickEvent<Button>> editListener) {
        Div actions = new Div();
        Button edit = new Button();
        edit.setIcon(VaadinIcon.EDIT.create());
        edit.addClickListener(editListener);
        actions.add(edit);
        return actions;
    }

    public static Div makeColumnAction(ComponentEventListener<ClickEvent<Button>> listener, VaadinIcon icon) {
        Div actions = new Div();
        Button action = new Button();
        action.setIcon(icon.create());
        action.addClickListener(listener);
        actions.add(action);
        return actions;
    }

    public static Div createDetailsColumnButton(ComponentEventListener<ClickEvent<Button>> clickListener) {
        Div actions = new Div();
        Button details = new Button("ПОДРОБНЕЙ");
        details.addClickListener(clickListener);
        actions.add(details);
        return actions;
    }

    public static Dialog initConfirmDialog(String message) {
        Dialog dialog = new Dialog();
        Div textDiv = new Div();
        textDiv.getStyle().set("padding", "10px");
        Text text = new Text(message);
        textDiv.add(text);
        dialog.setCloseOnEsc(false);
        dialog.setCloseOnOutsideClick(false);
        dialog.add(textDiv);
        Button confirmButton = new Button("Да", event -> dialog.close());
        Button cancelButton = new Button("Нет", event -> dialog.close());
        Div buttons = new Div(confirmButton, cancelButton);
        buttons.getStyle()
                .set("display", "flex")
                .set("justify-content", "flex-end");
        dialog.add(buttons);
        return dialog;
    }

    // в vaadin такая особенность, можно указать картинки, которые лежат в определённых папках (VAADIN/STATIC/IMAGES)
    // точно не помню путь, но он задан довольно жёстко, или делать это как здесь динамически
    private static StreamResource createFileResource(File file) {
        StreamResource sr = new StreamResource("", (InputStreamFactory) () -> {
            try {
                if (!Files.exists(file.toPath())) {
                    return new FileInputStream(Objects.requireNonNull(getDefaultAvatar()));
                } else {
                    return new FileInputStream(file);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
        sr.setCacheTime(0);
        return sr;
    }

    // если у пользователя нет аватара
    private static File getDefaultAvatar() {
        try {
            ClassPathResource classPathResource = new ClassPathResource("static/images/no-avatar2.png");
            File file = File.createTempFile("tmp", ".png");
            file.deleteOnExit();
            InputStream inputStream = classPathResource.getInputStream();
            FileUtils.copyInputStreamToFile(inputStream, file);
            return file;
        } catch (IOException e) {
            LOGGER.error("Изображение по умолчанию не найдено!", e);
            return null;
        }
    }


    // использовалось для вывода сообщений, если к примеру корзина пустая
    public static Div createInfoDiv(String message) {
        Div div = new Div();
        div.setSizeFull();
        Span span = new Span(message);
        div.add(span);
        div.getStyle()
                .set("display", "flex")
                .set("justify-content", "center")
                .set("align-items", "center")
                .set("font-size", "xx-large");
        return div;
    }

    public static Button createButton(String text, String iconType, String buttonType, String padding) {
        if (padding.isEmpty()) padding = "8px 10px 25px 10px";
        String iconPadding = "";
        Button button = new Button(text);
        String bgColor = "submit".equalsIgnoreCase(buttonType) ? "bg-green" :
                "delegate".equalsIgnoreCase(buttonType) ? "bg-deep-orange" :
                        "regular".equalsIgnoreCase(buttonType) ? "bg-blue" : "bg-red";
        if ("delegate".equalsIgnoreCase(buttonType) || "regular".equalsIgnoreCase(buttonType)) iconPadding = "padding-bottom: 10px";
        button.addClassNames("btn", "btn-lg", bgColor, "waves-effect");
        button.getStyle().set("padding", padding);
        Html icon = new Html("<i class=\"material-icons\" style=\"" + iconPadding + "\">" + iconType + "</i>");
        button.setIcon(icon);
        return button;
    }

    public static Image getLogo(int sizeInPx) {
        Image img = new Image("icons/logo.png", "ДДКолесникъ");
        img.setHeight(sizeInPx + "px");
        return img;
    }

    public static void showNotification(String text) {
        Notification notification = new Notification(text, 2_000, Notification.Position.TOP_END);
        notification.open();
    }

}
