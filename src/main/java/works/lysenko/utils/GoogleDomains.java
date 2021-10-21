package works.lysenko.utils;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

@SuppressWarnings("javadoc")
public class GoogleDomains {

    private static final List<Pair<String, Double>> d = new LinkedList<Pair<String, Double>>();

    private static Pair<String, Double> pair(String string, Double pair) {
        return new Pair<String, Double>(string, pair);
    }

    public static String getGoogleDomain() {
        return new EnumeratedDistribution<String>(d).sample();
    }

    static {
        // data source: https://www.google.com/supported_domains
        d.add(pair("google.com", 1.5));
        d.add(pair("google.aс", 0.0)); // absent in source, but available (!)
        d.add(pair("google.ad", 0.15));
        d.add(pair("google.ae", 0.85));
        d.add(pair("google.com.af", 0.15));
        d.add(pair("google.com.ag", 0.25));
        d.add(pair("google.com.ai", 0.65));
        d.add(pair("google.al", 0.15));
        d.add(pair("google.am", 0.25));
        d.add(pair("google.co.ao", 0.15));
        d.add(pair("google.com.ar", 0.85));
        d.add(pair("google.as", 0.65));
        d.add(pair("google.at", 0.95));
        d.add(pair("google.com.au", 0.55));
        d.add(pair("google.az", 0.15));
        d.add(pair("google.ba", 0.35));
        d.add(pair("google.com.bd", 0.25));
        d.add(pair("google.be", 0.95));
        d.add(pair("google.bf", 0.15));
        d.add(pair("google.bg", 0.35));
        d.add(pair("google.com.bh", 0.25));
        d.add(pair("google.bi", 0.15));
        d.add(pair("google.bj", 0.25));
        d.add(pair("google.com.bn", 0.15));
        d.add(pair("google.com.bo", 0.25));
        d.add(pair("google.bq", 0.0)); // not available
        d.add(pair("google.com.br", 0.95));
        d.add(pair("google.bs", 0.85));
        d.add(pair("google.bt", 0.15));
        d.add(pair("google.co.bw", 0.15));
        d.add(pair("google.by", 0.05));
        d.add(pair("google.com.bz", 0.25));
        d.add(pair("google.ca", 0.95));
        d.add(pair("google.cc", 0.0)); // available, but redirected to google.com
        d.add(pair("google.cd", 0.15));
        d.add(pair("google.cf", 0.25));
        d.add(pair("google.cg", 0.15));
        d.add(pair("google.ch", 0.99));
        d.add(pair("google.ci", 0.25));
        d.add(pair("google.co.ck", 0.25));
        d.add(pair("google.cl", 0.65));
        d.add(pair("google.cm", 0.15));
        d.add(pair("google.cn", 0.0)); // available, but is not functional 
        d.add(pair("google.com.co", 0.25));
        d.add(pair("google.co.cr", 0.25));
        d.add(pair("google.com.cu", 0.15));
        d.add(pair("google.cv", 0.15));
        d.add(pair("google.com.cy", 0.45));
        d.add(pair("google.cz", 0.35));
        d.add(pair("google.de", 0.95));
        d.add(pair("google.dj", 0.15));
        d.add(pair("google.dk", 0.85));
        d.add(pair("google.dm", 0.25));
        d.add(pair("google.com.do", 0.25));
        d.add(pair("google.dz", 0.15));
        d.add(pair("google.com.ec", 0.15));
        d.add(pair("google.ee", 0.55));
        d.add(pair("google.com.eg", 0.55));
        d.add(pair("google.eh", 0.0)); // not available
        d.add(pair("google.er", 0.0)); // not available
        d.add(pair("google.es", 0.85));
        d.add(pair("google.com.et", 0.15));
        d.add(pair("google.eu", 0.0)); // available, but redirected to google.co.uk
        d.add(pair("google.fi", 0.95));
        d.add(pair("google.com.fj", 0.55));
        d.add(pair("google.fm", 0.25));
        d.add(pair("google.fo", 0.0)); // available, but shows a stub
        d.add(pair("google.fr", 0.95));
        d.add(pair("google.ga", 0.15));
        d.add(pair("google.ge", 0.55));
        d.add(pair("google.gf", 0.00)); // available, but redirected to google.com
        d.add(pair("google.gg", 0.45));
        d.add(pair("google.com.gh", 0.15));
        d.add(pair("google.com.gi", 0.75));
        d.add(pair("google.gl", 0.45));
        d.add(pair("google.gm", 0.15));
        d.add(pair("google.gn", 0.00)); // not available
        d.add(pair("google.gp", 0.75)); // absent in source, but available (!)
        d.add(pair("google.gq", 0.00)); // registered by Google, but not available
        d.add(pair("google.gr", 0.65));
        d.add(pair("google.gs", 0.00)); // registered by Google, but not available
        d.add(pair("google.com.gt", 0.15));
        d.add(pair("google.gy", 0.15));
        d.add(pair("google.com.hk", 0.95));
        d.add(pair("google.hm", 0.5)); // not available, whois lookup not working
        d.add(pair("google.hn", 0.15));
        d.add(pair("google.hr", 0.55));
        d.add(pair("google.ht", 0.65));
        d.add(pair("google.hu", 0.55));
        d.add(pair("google.co.id", 0.45));
        d.add(pair("google.ie", 0.95));
        d.add(pair("google.co.il", 0.95));
        d.add(pair("google.im", 0.75));
        d.add(pair("google.co.in", 0.75));
        d.add(pair("google.io", 0.0)); // available, but registered to Markmonitor Inc. and returns page 404
        d.add(pair("google.iq", 0.15));
        d.add(pair("google.ir", 0.00)); // registered by Google, but not available
        d.add(pair("google.is", 0.85));
        d.add(pair("google.it", 0.85));
        d.add(pair("google.je", 0.75));
        d.add(pair("google.com.jm", 0.65));
        d.add(pair("google.jo", 0.35));
        d.add(pair("google.co.jp", 0.95));
        d.add(pair("google.co.ke", 0.15));
        d.add(pair("google.com.kh", 0.15));
        d.add(pair("google.ki", 0.15)); // these two are out of alphabetical order
        d.add(pair("google.kg", 0.25)); // in the source data
        d.add(pair("google.km", 0.00)); // not available
        d.add(pair("google.kn", 0.00)); // registered by Google, but not available
        d.add(pair("google.kp", 0.00)); // not available
        d.add(pair("google.co.kr", 0.85));
        d.add(pair("google.com.kw", 0.25));
        d.add(pair("google.kz", 0.25));
        d.add(pair("google.la", 0.15));
        d.add(pair("google.com.lb", 0.15));
        d.add(pair("google.li", 0.85));
        d.add(pair("google.lk", 0.65));
        d.add(pair("google.lr", 0.00)); // not available
        d.add(pair("google.co.ls", 0.15));
        d.add(pair("google.lt", 0.75));
        d.add(pair("google.lu", 0.95));
        d.add(pair("google.lv", 0.75));
        d.add(pair("google.com.ly", 0.15));
        d.add(pair("google.co.ma", 0.15));
        d.add(pair("google.mc", 0.0)); // not available
        d.add(pair("google.md", 0.55));
        d.add(pair("google.me", 0.75));
        d.add(pair("google.mg", 0.65));
        d.add(pair("google.mk", 0.75));
        d.add(pair("google.ml", 0.15));
        d.add(pair("google.com.mm", 0.05));
        d.add(pair("google.mn", 0.25));
        d.add(pair("google.mo", 0.0)); // not available
        d.add(pair("google.mp", 0.0)); // not available
        d.add(pair("google.mq", 0.0)); // registered by Google, but not available
        d.add(pair("google.mr", 0.0)); // registered by "iPord Services", not available
        d.add(pair("google.ms", 0.75));
        d.add(pair("google.com.mt", 0.55));
        d.add(pair("google.mu", 0.15));
        d.add(pair("google.mv", 0.75));
        d.add(pair("google.mw", 0.15));
        d.add(pair("google.com.mx", 0.75));
        d.add(pair("google.com.my", 0.65));
        d.add(pair("google.co.mz", 0.15));
        d.add(pair("google.com.na", 0.15));
        d.add(pair("google.nc", 0.0)); // not available
        d.add(pair("google.nf", 0.0)); // registered by Google, but not available
        d.add(pair("google.com.ng", 0.15));
        d.add(pair("google.com.ni", 0.15));
        d.add(pair("google.ne", 0.0)); // not in alphabetical order in original list, and not available (!)
        d.add(pair("google.nl", 0.85));
        d.add(pair("google.no", 0.95));
        d.add(pair("google.com.np", 0.15));
        d.add(pair("google.nr", 0.15));
        d.add(pair("google.nu", 0.15));
        d.add(pair("google.co.nz", 0.85));
        d.add(pair("google.com.om", 0.15));
        d.add(pair("google.com.pa", 0.65));
        d.add(pair("google.com.pe", 0.55));
        d.add(pair("google.pf", 0.0)); // available, but redirected to google.com
        d.add(pair("google.com.pg", 0.15));
        d.add(pair("google.com.ph", 0.85));
        d.add(pair("google.com.pk", 0.55));
        d.add(pair("google.pl", 0.75));
        d.add(pair("google.pm", 0.0)); // registered by Google, but not available
        d.add(pair("google.pn", 0.15));
        d.add(pair("google.com.pr", 0.75));
        d.add(pair("google.ps", 0.05));
        d.add(pair("google.pt", 0.85));
        d.add(pair("google.com.py", 0.15));
        d.add(pair("google.com.qa", 0.55));
        d.add(pair("google.re", 0.0)); // available, but redirected to google.fr
        d.add(pair("google.ro", 0.65));
        d.add(pair("google.ru", 0.01));
        d.add(pair("google.rw", 0.15));
        d.add(pair("google.com.sa", 0.95));
        d.add(pair("google.com.sb", 0.55));
        d.add(pair("google.sc", 0.85));
        d.add(pair("google.se", 0.95));
        d.add(pair("google.com.sg", 0.95));
        d.add(pair("google.sh", 0.75));
        d.add(pair("google.si", 0.55));
        d.add(pair("google.sk", 0.55));
        d.add(pair("google.com.sl", 0.15));
        d.add(pair("google.sn", 0.15));
        d.add(pair("google.so", 0.15));
        d.add(pair("google.sm", 0.85));
        d.add(pair("google.sr", 0.15));
        d.add(pair("google.ss", 0.0)); // not available (technically)
        d.add(pair("google.st", 0.15));
        d.add(pair("google.su", 0.0)); // registered to private person, irrelevant http://google.su site
        d.add(pair("google.com.sv", 0.15));
        d.add(pair("google.sx", 0.0)); // registered by Google, not available
        d.add(pair("google.sy", 0.0)); // not available
        d.add(pair("google.sz", 0.0)); // not available
        d.add(pair("google.td", 0.05));
        d.add(pair("google.tf", 0.0)); // available, but shows a stub
        d.add(pair("google.tg", 0.15));
        d.add(pair("google.co.th", 0.65));
        d.add(pair("google.com.tj", 0.25));
        d.add(pair("google.tk", 0.55)); // absent in source, but available (!)
        d.add(pair("google.tl", 0.35));
        d.add(pair("google.tm", 0.25));
        d.add(pair("google.tn", 0.15));
        d.add(pair("google.to", 0.15));
        d.add(pair("google.com.tr", 0.95));
        d.add(pair("google.tt", 0.15));
        d.add(pair("google.tv", 0.0)); // registered by Google, not available
        d.add(pair("google.com.tw", 0.95));
        d.add(pair("google.co.tz", 0.15));
        d.add(pair("google.com.ua", 0.75));
        d.add(pair("google.co.ug", 0.15));
        d.add(pair("google.co.uk", 0.95));
        d.add(pair("google.us", 0.0)); // available, but redirected to google.com
        d.add(pair("google.com.uy", 0.15));
        d.add(pair("google.co.uz", 0.25));
        d.add(pair("google.va", 0.0)); // not available
        d.add(pair("google.com.vc", 0.25));
        d.add(pair("google.co.ve", 0.05));
        d.add(pair("google.vg", 0.65));
        d.add(pair("google.co.vi", 0.75));
        d.add(pair("google.com.vn", 0.25));
        d.add(pair("google.vu", 0.15));
        d.add(pair("google.ws", 0.25));
        d.add(pair("google.rs", 0.01)); // dramatically out of order in source data
        d.add(pair("google.ye", 0.0)); // not available
        d.add(pair("google.yt", 0.0)); // available, but shows a stub
        d.add(pair("google.co.za", 0.15));
        d.add(pair("google.co.zm", 0.15));
        d.add(pair("google.co.zw", 0.15));
        d.add(pair("google.cat", 0.55)); // [not a ccTLD]
    }
}
