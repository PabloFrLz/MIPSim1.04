package GUI;

import javafx.scene.Node;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DisplacementMap;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.effect.MotionBlur;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.effect.Reflection;
import javafx.scene.effect.SepiaTone;
import javafx.scene.effect.Shadow;
import javafx.scene.paint.Color;

public class Effects {
    
    // Efeitos adicionais podem ser encontrados nessa classe para ser usado como bem entender.
    // demais efeitos podem ser encontrados nas demais classes como.
    // o return dos metodos não precisa ser capturado, basta acessar staticamente e pronto: "Effects.efeitoReflection()".
    // o return foi inserido nos metodos para poder usar o metodo 'efeitoBlend()', que demanda dois efeitos para combiná-los

    public Effects(){}


    public static Reflection efeitoReflection(Node objeto){ //espelhamento
        Reflection reflection = new Reflection();
        reflection.setFraction(1.0);  // Define a fração do comprimento da reflexão
        reflection.setTopOffset(5.0); // Define a distância entre o objeto e sua reflexão
        objeto.setEffect(reflection);
        return reflection;
    }

    public static Bloom efeitoBloom(Node objeto){
        Bloom bloom = new Bloom();
        bloom.setThreshold(0.1);
        objeto.setEffect(bloom);
        return bloom;
    }

    public static BoxBlur efeitoBoxBlur(Node objeto){
        BoxBlur boxBlur = new BoxBlur();
        boxBlur.setWidth(6);
        boxBlur.setHeight(6);
        boxBlur.setIterations(2);
        objeto.setEffect(boxBlur);
        return boxBlur;
    }

    public static DisplacementMap efeitoDisplacement(Node objeto){
        DisplacementMap displacementMap = new DisplacementMap();
        displacementMap.setScaleX(0.1);
        displacementMap.setScaleY(0.1);
        objeto.setEffect(displacementMap);
        return displacementMap;
    }

    public static GaussianBlur efeitoGaussianBlur(Node objeto){
        GaussianBlur gaussianBlur = new GaussianBlur();
        gaussianBlur.setRadius(10);
        objeto.setEffect(gaussianBlur);
        return gaussianBlur;
    }

    public static Glow efeitoGlow(Node objeto){
        Glow glow = new Glow();
        glow.setLevel(1.0);
        objeto.setEffect(glow);
        return glow;
    }

    public static InnerShadow efeitoInnerShadow(Node objeto){
        InnerShadow innerShadow = new InnerShadow();
        innerShadow.setOffsetX(5);
        innerShadow.setOffsetY(5);
        innerShadow.setColor(Color.GRAY);
        objeto.setEffect(innerShadow);
        return innerShadow;
    }

    public static Lighting efeitoLighting(String type, Node objeto){
        Lighting lighting = new Lighting();
        if(type.equals("Distant")){
            Light.Distant light = new Light.Distant();
            light.setAzimuth(45);
            light.setElevation(30);
            lighting.setLight(light);

        } else if(type.equals("Point")){
            Light.Point light = new Light.Point();
            light.setX(50);
            light.setY(50);
            light.setZ(50);
            lighting.setLight(light);

        } else if(type.equals("Spot")){
            Light.Spot light = new Light.Spot();
            light.setX(50);
            light.setY(50);
            light.setZ(50);
            light.setPointsAtX(100);
            light.setPointsAtY(100);
            light.setSpecularExponent(2);
            lighting.setLight(light);
        }  

        objeto.setEffect(lighting);
        return lighting;
    }

    public static MotionBlur efeitoMotionBlur(Node objeto){
        MotionBlur motionBlur = new MotionBlur();
        motionBlur.setAngle(45);
        motionBlur.setRadius(10);
        objeto.setEffect(motionBlur);
        return motionBlur;
    }

    public static PerspectiveTransform efeitoPerspectiveTransform(Node objeto){
        PerspectiveTransform perspectiveTransform = new PerspectiveTransform();
        perspectiveTransform.setUlx(0);
        perspectiveTransform.setUly(0);
        perspectiveTransform.setUrx(200);
        perspectiveTransform.setUry(20);
        perspectiveTransform.setLlx(0);
        perspectiveTransform.setLly(200);
        perspectiveTransform.setLrx(200);
        perspectiveTransform.setLry(180);
        objeto.setEffect(perspectiveTransform);
        return perspectiveTransform;

    }

    public static SepiaTone efeitoSepiaTone(Node objeto){
        SepiaTone sepiaTone = new SepiaTone();
        sepiaTone.setLevel(0.8);
        objeto.setEffect(sepiaTone);
        return sepiaTone;

    }

    public static Shadow efeitoShadow(Node objeto){
        Shadow shadow = new Shadow();
        shadow.setRadius(10);
        shadow.setColor(Color.GRAY);
        objeto.setEffect(shadow);
        return shadow;
    }

    public static ColorAdjust efeitoColorAdjust(Node objeto){
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(0.2);  // Ajuste de brilho
        colorAdjust.setContrast(0.3);    // Ajuste de contraste
        colorAdjust.setHue(0.1);         // Ajuste de matiz
        colorAdjust.setSaturation(0.5);  // Ajuste de saturação
        objeto.setEffect(colorAdjust);
        return colorAdjust;
    }

    // não confundir esta classe "Effects" com a classe do javafx "Effect"
    public static void efeitoBlend(Effect efeito1, Effect efeito2){ //da pra combinar dois efeitos diferentes
        Blend blend = new Blend();
        blend.setMode(BlendMode.MULTIPLY);  // Define o modo de blend
        blend.setTopInput(efeito1);
        blend.setBottomInput(efeito2);
    }

    public static void efeitoDropShadow(Node objeto){
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(10.0);
        dropShadow.setOffsetX(1.0);
        dropShadow.setOffsetY(1.0);
        dropShadow.setColor(Color.BLACK);
        objeto.setEffect(dropShadow);
    }
}
