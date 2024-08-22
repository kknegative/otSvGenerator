package org.otsvgenerator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.otsvgenerator.entity.BaseDO;
import org.otsvgenerator.entity.HitObjectsDO;
import org.otsvgenerator.entity.TimingPtsDO;
import org.otsvgenerator.entity.request.BaseBatchGenerateRequest;
import org.otsvgenerator.entity.request.SvBatchGenerateRequest;
import org.otsvgenerator.generator.SvPtsGenerator;
import org.otsvgenerator.generator.TimingPtsGenerator;
import org.otsvgenerator.parser.HitObjectsParser;
import org.otsvgenerator.parser.TimestampParser;
import org.otsvgenerator.parser.TimingPtsParser;



public class Main {
    public static void main(String[] args) {
        String tsStr = "212,88,107022,1,0,0:0:0:0:\n217,115,107096,1,0,0:0:0:0:\n214,128,107245,1,0,0:0:0:0:\n214,128,107393,1,0,0:0:0:0:\n214,128,107467,1,0,0:0:0:0:\n209,124,107763,1,0,0:0:0:0:\n209,124,107837,1,0,0:0:0:0:\n217,65,108133,1,0,0:0:0:0:\n256,176,108207,1,0,0:0:0:0:\n256,176,108282,1,0,0:0:0:0:\n212,88,108800,1,0,0:0:0:0:\n217,115,108874,1,0,0:0:0:0:\n214,128,109023,1,0,0:0:0:0:\n214,128,109171,1,0,0:0:0:0:\n214,128,109245,1,0,0:0:0:0:\n251,93,110652,1,0,0:0:0:0:\n251,93,110726,1,0,0:0:0:0:\n251,93,110948,1,0,0:0:0:0:\n251,93,111022,1,0,0:0:0:0:\n251,93,111541,1,0,0:0:0:0:\n251,93,111763,1,0,0:0:0:0:\n251,93,111837,1,0,0:0:0:0:\n257,154,112430,1,0,0:0:0:0:\n257,154,112504,1,0,0:0:0:0:\n257,154,112726,1,0,0:0:0:0:\n257,154,112800,1,0,0:0:0:0:\n257,154,114207,1,0,0:0:0:0:\n257,154,114282,1,0,0:0:0:0:\n257,154,114430,1,0,0:0:0:0:\n257,154,114504,1,0,0:0:0:0:\n263,187,114874,1,0,0:0:0:0:\n263,187,115096,1,0,0:0:0:0:\n263,187,115319,1,0,0:0:0:0:\n263,187,115393,1,0,0:0:0:0:\n270,172,115985,1,0,0:0:0:0:\n270,172,116207,1,0,0:0:0:0:\n270,172,116282,1,0,0:0:0:0:\n270,172,116356,1,0,0:0:0:0:\n270,172,116430,1,0,0:0:0:0:\n270,172,116652,1,0,0:0:0:0:\n270,172,116800,1,0,0:0:0:0:\n270,172,117022,1,0,0:0:0:0:\n270,172,117096,1,0,0:0:0:0:\n277,181,117911,1,0,0:0:0:0:\n282,199,118133,1,0,0:0:0:0:\n294,194,118874,1,0,0:0:0:0:\n294,194,119022,1,0,0:0:0:0:\n287,202,119393,1,0,0:0:0:0:\n276,192,119763,1,0,0:0:0:0:\n276,192,119837,1,0,0:0:0:0:\n277,192,120163,1,0,0:0:0:0:\n287,202,120252,1,0,0:0:0:0:\n181,168,120341,1,0,0:0:0:0:\n181,168,120430,1,0,0:0:0:0:\n237,186,120696,1,0,0:0:0:0:\n";
        HitObjectsParser parser = new HitObjectsParser();
        List<HitObjectsDO> lst = parser.deserialize(tsStr);
        TimingPtsGenerator timingPtsGenerator = new TimingPtsGenerator();
        SvPtsGenerator svPtsGenerator = new SvPtsGenerator();
        List<TimingPtsDO> generatedObjects = new ArrayList<>();
        Random random = new Random();
        for (HitObjectsDO obj : lst) {
            BaseBatchGenerateRequest baseReq = new BaseBatchGenerateRequest();
            baseReq.setStart(obj.getTimestamp());
            baseReq.setEnd(obj.getTimestamp());
            baseReq.setBpm(new BigDecimal("135"));
            baseReq.setSnap(1);
            baseReq.setVolume(0);
            baseReq.setBeats(4);
            baseReq.setUseSoft(false);
            baseReq.setInKiai(false);
            List<TimingPtsDO> timingPtsDOList = timingPtsGenerator.batchGenerate(baseReq);
            generatedObjects.addAll(timingPtsDOList);
            SvBatchGenerateRequest svReq = new SvBatchGenerateRequest();
            svReq.setSvStart(random.nextDouble() * 0.3 + 0.3);
            svReq.setStep(0.0D);
            svReq.setStart(obj.getTimestamp());
            svReq.setEnd(obj.getTimestamp());
            svReq.setBpm(new BigDecimal("135"));
            svReq.setSnap(1);
            svReq.setVolume(0);
            svReq.setBeats(4);
            svReq.setUseSoft(false);
            svReq.setInKiai(false);
            List<TimingPtsDO> svPtsDOList = svPtsGenerator.batchGenerate(svReq);
            generatedObjects.addAll(svPtsDOList);
        }
        generatedObjects.sort(Comparator.comparing(TimingPtsDO::getTimestamp));
        TimingPtsParser timingPtsParser = new TimingPtsParser();
        String s = timingPtsParser.serialize(generatedObjects);
        System.out.println(s);
    }
}
