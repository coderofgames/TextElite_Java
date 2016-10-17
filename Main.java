/*
 * Main.java
 *
 * Created on 26 September 2007, 16:04
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package javaapplication2;

import java.util.*;
import java.io.*;
/**
 *
 * @author dave
 */
public class Main {
    
    public class elite{
        
        elite(){}
        
        //public boolean true = (-1);
        //public boolean false = (0);
        int tonnes = 0;
        
        int maxlen = 20; /* Length of strings */
             
        //typedef int /*planetnum*/int;
        
        public class fastseedtype {
            public char a,b,c,d;
        }  /* four byte random number used for planet description */
        
        
        public class seedtype {
            public int w0;
            public int w1;
            public int w2;
        }   /* six byte random number used as seed for planets */
        
        public class plansys {
            public int x;
            public int y;       /* One byte unsigned */
            public int economy; /* These two are actually only 0-7  */
            public int govtype;
            public int techlev; /* 0-16 i think */
            public int population;   /* One byte */
            public int productivity; /* Two byte */
            public int radius; /* Two byte (not used by game at all) */
            public fastseedtype	goatsoupseed = new fastseedtype();
            public char []tname = new char[12];
            public String name = new String();
        }
        
        int galsize = 256;
        int AlienItems = 16;
        int lasttrade = AlienItems;
        
        int numforLave = 7;       /* Lave is 7th generated planet in galaxy one */
        int numforZaonce = 129;
        int numforDiso =147;
        int numforRied =46;
        
        plansys [] galaxy = new plansys[galsize]; /* Need 0 to galsize-1 inclusive */
        
        seedtype seed = new seedtype();
        
        fastseedtype rnd_seed = new fastseedtype();
        
        boolean nativerand = false;
        
        public class tradegood {
            public tradegood(int bp, short grad, int bq, int mb, int unit, String nm) {
                baseprice = bp;
                gradient = grad;
                basequant = bq;
                maskbyte = mb;
                units = unit;
                name = nm;
            }
            /* In 6502 version these were: */
            public int baseprice;        /* one byte */
            public short gradient;   /* five bits plus sign */
            public int basequant;        /* one byte */
            public int maskbyte;         /* one byte */
            public int units;            /* two bits */
            //char   name[20];         /* longest="Radioactives" */
            public String name = new String();
        }
        
        
        public class markettype {
            public int [] quantity = new int[lasttrade+1];
            public int [] price = new int[lasttrade+1];
        }
        
        /* Player workspace */
        int     [] shipshold = new int[lasttrade+1];  /* Contents of cargo bay */
        /*planetnum*/int  currentplanet;           /* Current planet */
        int     galaxynum;               /* Galaxy number (1-8) */
        int    cash;
        int     fuel;
        markettype localmarket;
        int     holdspace;
        
        int fuelcost =2; /* 0.2 CR/Light year */
        int maxfuel =70; /* 7.0 LY tank */
        
        char base0=0x5A4A;
        char base1=0x0248;
        char base2=0xB753;  /* Base seed for galaxy 1 */
        
        
//static const char *digrams=
//							 "ABOUSEITILETSTONLONUTHNO"
//							 "ALLEXEGEZACEBISO"
//							 "USESARMAINDIREA?"
//							 "ERATENBERALAVETI"
//							 "EDORQUANTEISRION";
        
        String pairs0="ABOUSEITILETSTONLONUTHNO";
        
        /* must continue into .. */
        String pairs = "..LEXEGEZACEBISOUSESARMAINDIREA.ERATENBERALAVETIEDORQUANTEISRION"; /* Dots should be nullprint characters */
        /*
        char pairs[] =  "..LEXEGEZACEBISO"
                        "USESARMAINDIREA."
                        "ERATENBERALAVETI"
                        "EDORQUANTEISRION"; /* Dots should be nullprint characters */
        
        String [] govnames ={"Anarchy","Feudal","Multi-gov","Dictatorship",
        "Communist","Confederacy","Democracy","Corporate State"};
        
        String [] econnames ={"Rich Ind","Average Ind","Poor Ind","Mainly Ind",
        "Mainly Agri","Rich Agri","Average Agri","Poor Agri"};
        
        
        String [] unitnames ={"t","kg","g"};
        
        /* Data for DB's price/availability generation system */
        /*                   Base  Grad Base Mask Un   Name
                            price ient quant     it              */
        
        int POLITICALLY_CORRECT	= 0;
        /* Set to 1 for NES-sanitised trade goods */
        
        /* Set to 1 for NES-sanitised trade goods */
        
        public tradegood [] commodities=
        {
            new tradegood( 0x13,(short)-0x02,0x06,0x01,0,"Food"),
            new tradegood( 0x14,(short)-0x01,0x0A,0x03,0,"Textiles"),
            new tradegood( 0x41,(short)-0x03,0x02,0x07,0,"Radioactives"),
/*#if POLITICALLY_CORRECT*/
/*           
            new tradegood( 0x28,(short)-0x05,0xE2,0x1F,0,"Robot Slaves"},
            new tradegood( 0x53,(short)-0x05,0xFB,0x0F,0,"Beverages   "},*/
/*#else*/
            new tradegood( 0x28,(short)-0x05,0xE2,0x1F,0,"Slaves"),
            new tradegood( 0x53,(short)-0x05,0xFB,0x0F,0,"Liquor/Wines"),
            /*#endif */
            new tradegood( 0xC4,(short)+0x08,0x36,0x03,0,"Luxuries"),
/*#if POLITICALLY_CORRECT
            new tradegood( 0xEB,+0x1D,0x08,0x78,0,"Rare Species"),
#else*/
            new tradegood( 0xEB,(short)+0x1D,0x08,0x78,0,"Narcotics"),
            /*#endif*/
            new tradegood( 0x9A,(short)+0x0E,0x38,0x03,0,"Computers"),
            new tradegood( 0x75,(short)+0x06,0x28,0x07,0,"Machinery"),
            new tradegood( 0x4E,(short)+0x01,0x11,0x1F,0,"Alloys"),
            new tradegood( 0x7C,(short)+0x0d,0x1D,0x07,0,"Firearms"),
            new tradegood( 0xB0,(short)-0x09,0xDC,0x3F,0,"Furs"),
            new tradegood( 0x20,(short)-0x01,0x35,0x03,0,"Minerals"),
            new tradegood( 0x61,(short)-0x01,0x42,0x07,1,"Gold"),
            new tradegood( 0xAB,(short)-0x02,0x37,0x1F,1,"Platinum"),
            new tradegood( 0x2D,(short)-0x01,0xFA,0x0F,2,"Gem-Strones"),
            new tradegood( 0x35,(short)+0x0F,0xC0,0x07,0,"Alien Items "),
        };
        
        /**-Required data for text interface **/
        String [] tradnames = new String[16]; /* Tradegood names used in text commands
                                      Set using commodities array */
        
        public int nocomms = 14;
        
        
        String [] commands={"buy",      "sell",     "fuel",     "jump",
                            "cash",     "mkt",      "help",     "hold",
                            "sneak",    "local",    "info",     "galhyp",
                            "quit",     "rand"};
        
        /*boolean (*comfuncs[nocomms])(char *)=
        {dobuy,         dosell,       dofuel,    dojump,
        docash,        domkt,        dohelp,    dohold,
        dosneak,       dolocal,      doinfo,    dogalhyp,
        doquit,				 dotweakrand
        };  */
        
        int lastrand = 0;
        
        //void port_srand(unsigned int);
        //int port_rand(void);
        
        void mysrand( int seed) {
            //srand(seed);
            lastrand = seed - 1;
        }
        
        int myrand() {
            int r;
            if(nativerand) r=(int)Math.random();
            else {	// As supplied by D McDonnell	from SAS Insititute C
                r = (((((((((((lastrand << 3) - lastrand) << 3)
                + lastrand) << 1) + lastrand) << 4)
                - lastrand) << 1) - lastrand) + 0xe60)
                & 0x7fffffff;
                lastrand = r - 1;
            }
            return r;
        }
        
        
        char randbyte() {
            return (char)(myrand()&0xFF);
        }
        
        int mymin(int a,int b) {
            if(a<b)
                return(a);
            else return(b);
        }
        
        /**+  ftoi **/
        int ftoi(double value) {
            return (( int)Math.floor(value+0.5));
        }
        
        /**+  ftoi2 **/
        int ftoi2(double value) {
            return (( int)Math.floor(value));
        }
        
        void tweakseed(seedtype s) {
            char temp;
            temp = (char)((s.w0)+(s.w1)+(s.w2)); /* 2 byte aritmetic */
            s.w0 = s.w1;
            s.w1 = s.w2;
            s.w2 = temp;
        }
        
        
        /**-String functions for text interface **/
        
        
        // done - this is done as:- s = s.replaceAll(new String(c), "");
        // have also removed spacesplit
        void stripout(String s, char c) /* Remove all c's from string s */
        {
            char[] arr = new char[1];
            arr[0] = c;
            String str = new String(arr);
            s.replaceAll(str, "");
            
        }
        
        // not a problem here
        int toupper(char c) {
            if((c>='a')&&(c<='z')) return(c+'A'-'a');
            return (int)c;
        }
        
        // not a problem
        int tolower(char c) {
            if((c>='A')&&(c<='Z')) return(c+'a'-'A');
            return (int)(c);
        }
        
        
        boolean stringbeg(String s,String t)
        /* Return nonzero iff string t begins with non-empty string s */
        {
            int i=0;
            int l=s.length();
            int l2 = t.length();
            if( l > l2) return false;
            
            String s2 = t.substring(0,s.length());
            if( s.compareTo(s2)==0)return true;
            
            /*if(l>0&&l2 >0) {
                while((i<l)&(toupper(s.charAt(i))==toupper( t.charAt(i) )))	i++;
                //if(s.charAt(i) == t.charAt(i) )
                    if(i==l) return true;
            }*/
            return false;
        }
        
        int stringmatch( String s, String[] a,int n)
        /* Check string s against n options in string array a
           If matches ith element return i+1 else return 0 
        */
        {
            int i=0;
            while(i<n) {
                if( s.compareTo(a[i].toUpperCase()) == 0  )return i+1;
                // if(stringbeg(s,a[i])) return i+1;
                i++;
            }
            return 0;
        }
        
        /**-Functions for stock market **/
        int gamebuy(int i, int a)
        /* Try to buy ammount a  of good i  Return ammount bought */
        /* Cannot buy more than is availble, can afford, or will fit in hold */
        {   int t;
            if(cash<0) t=0;
            else {
                t=mymin(localmarket.quantity[i],a);
                if ((commodities[i].units)==tonnes) {t = mymin(holdspace,t);}
                t = mymin(t, (int)Math.floor((double)cash/(localmarket.price[i])));
            }
            shipshold[i]+=t;
            localmarket.quantity[i]-=t;
            cash-=t*(localmarket.price[i]);
            if ((commodities[i].units)==tonnes) {holdspace-=t;}
            return t;
        }
        
        int gamesell(int i,int a) /* As gamebuy but selling */
        {   int t=mymin(shipshold[i],a);
            shipshold[i]-=t;
            localmarket.quantity[i]+=t;
            if ((commodities[i].units)==tonnes) {holdspace+=t;}
            cash+=t*(localmarket.price[i]);
            return t;
        }
        
        public markettype genmarket(int fluct, plansys p)
        /* Prices and availabilities are influenced by the planet's economy type
            (0-7) and a random "fluctuation" byte that was kept within the saved
            commander position to keep the market prices constant over gamesaves.
            Availabilities must be saved with the game since the player alters them
            by buying (and selling(?))
     
            Almost all operations are one byte only and overflow "errors" are
            extremely frequent and exploited.
     
            Trade Item prices are held internally in a single byte=true value/4.
            The decimal point in prices is introduced only when printing them.
            Internally, all prices are integers.
            The player's cash is held in four bytes.
        */    
        {
            markettype market = new markettype();
            short i;
            for(i=0;i<=lasttrade;i++) {
                int q;
                int product = (p.economy)*(commodities[i].gradient);
                int changing = fluct & (commodities[i].maskbyte);
                q =  (commodities[i].basequant) + changing - product;
                q = q&0xFF;
                if((q&0x80) != 0) {q=0;};                       /* Clip to positive 8-bit */
                
                market.quantity[i] = (char)(q & 0x3F); /* Mask to 6 bits */
                
                q =  (commodities[i].baseprice) + changing + product;
                q = q & 0xFF;
                market.price[i] = (char) (q*4);
            }
            market.quantity[AlienItems] = 0; /* Override to force nonavailability */
            return market;
        }
        
        public void displaymarket(markettype m) {
            short i;
            for(i=0;i<=lasttrade;i++) {
                System.out.print("\n");
                System.out.print(commodities[i].name);
                System.out.print("   " + ((float)(m.price[i])/10));
                System.out.print("   " + m.quantity[i]);
                System.out.print(unitnames[commodities[i].units]);
                System.out.print("   " + shipshold[i]);
            }
        }
        
        /**-Generate system info from seed **/  
        plansys makesystem(seedtype s) {
            
            plansys thissys = new plansys();
            int pair1,pair2,pair3,pair4;
            char longnameflag=(char)((s.w0)&64);
            
            thissys.x=((s.w1)>>8);
            thissys.y=((s.w0)>>8);
            
            thissys.govtype =(((s.w1)>>3)&7); /* bits 3,4 &5 of w1 */
            
            thissys.economy =(((s.w0)>>8)&7); /* bits 8,9 &A of w0 */
            if (thissys.govtype <=1) {
                thissys.economy = ((thissys.economy)|2);
            }
            
            thissys.techlev =(((s.w1)>>8)&3)+((thissys.economy)^7);
            thissys.techlev +=((thissys.govtype)>>1);
            if (((thissys.govtype)&1)==1)	thissys.techlev+=1;
            /* C simulation of 6502's LSR then ADC */
            
            thissys.population = 4*(thissys.techlev) + (thissys.economy);
            thissys.population +=  (thissys.govtype) + 1;
            
            thissys.productivity = (((thissys.economy)^7)+3)*((thissys.govtype)+4);
            thissys.productivity *= (thissys.population)*8;
            
            thissys.radius = 256*((((s.w2)>>8)&15)+11) + thissys.x;
            
            thissys.goatsoupseed.a = (char)( (s.w1) & 0xFF );
            thissys.goatsoupseed.b = (char)( (s.w1) >>8 );
            thissys.goatsoupseed.c = (char)( (s.w2) & 0xFF );
            thissys.goatsoupseed.d = (char)( (s.w2) >> 8 );
            
            pair1=2*(((short)(s.w2)>>8)&31);  tweakseed(s);
            pair2=2*(((short)(s.w2)>>8)&31);  tweakseed(s);
            pair3=2*(((short)(s.w2)>>8)&31);  tweakseed(s);
            pair4=2*(((short)(s.w2)>>8)&31);	tweakseed(s);
            /* Always four iterations of random number */
            
            thissys.tname[0] = pairs.charAt(pair1);
            thissys.tname[1] = pairs.charAt(pair1+1);
            thissys.tname[2] = pairs.charAt(pair2);
            thissys.tname[3] = pairs.charAt(pair2+1);
            thissys.tname[4] = pairs.charAt(pair3);
            thissys.tname[5]= pairs.charAt(pair3+1);
            
            if(longnameflag != 0) /* bit 6 of ORIGINAL w0 flags a four-pair name */
            {
                thissys.tname[6]=pairs.charAt(pair4);
                thissys.tname[7] =pairs.charAt(pair4+1);
                thissys.tname[8]=0;
            } else thissys.tname[6]=0;
            
            
            // some extra string work for this java implementation
            int len=0;
            for( int v=0;v<9;v++) {
                if(thissys.tname[v]=='\0'|| thissys.tname[v]=='.'){len=v;break;}
            }
            char [] newbuf = new char[len];
            for(int v=0;v<len;v++)newbuf[v]=thissys.tname[v];
            
            thissys.tname = new char[len];
            for(int v=0;v<len;v++)thissys.tname[v]=newbuf[v];
            
            //thissys.tname[len] ='\0';
            thissys.name = new String( newbuf );
            
            return thissys;
        }
        
        /**+Generate galaxy **/
        
        
        /* Functions for galactic hyperspace */
        
        char rotatel(int x) /* rotate 8 bit number leftwards */
        /* (tried to use chars but too much effort persuading this braindead
        language to do bit operations on bytes!) */
        { char temp = (char)(x&128);
          return (char)((2*(x&127))+(temp>>7));
        }
        
        char twist(int x) {
            return (char)(((256*rotatel((char)(x>>8)))+rotatel((char)(x&255))));
        }
        
        void nextgalaxy(seedtype s) /* Apply to base seed; once for galaxy 2  */
        { s.w0 = twist(s.w0);  /* twice for galaxy 3, etc. */
          s.w1 = twist(s.w1);  /* Eighth application gives galaxy 1 again*/
          s.w2 = twist(s.w2);
        }
        
        /* Original game generated from scratch each time info needed */
        void buildgalaxy(int galaxynum) {
            //System.out.print("\nEntering 'buildgalaxy'..\n");
            //System.out.print("\n");
            int syscount,galcount;
            seed.w0=base0; seed.w1=base1; seed.w2=base2; /* Initialise seed for galaxy 1 */
            for(galcount=1;galcount<galaxynum;++galcount) {
                nextgalaxy(seed);
            }
            /* Put galaxy data into array of structures */
            for(syscount=0;syscount<galsize;++syscount) {
                galaxy[syscount]=makesystem(seed);
            }
        }
        
        /**-Functions for navigation **/
        
        void gamejump(/*planetnum*/int i) /* Move to system i */
        { currentplanet=i;
          localmarket = genmarket(randbyte(),galaxy[i]);
        }
        
        int distance(plansys a,plansys b)
        /* Seperation between two planets (4*sqrt(X*X+Y*Y/4)) */
        {
            return (int)(ftoi(4*Math.sqrt((double)((a.x-b.x)*(a.x-b.x)+(a.y-b.y)*(a.y-b.y)/4))));
        }
        
        
        /*planetnum*/int matchsys(String s)
        /* Return id of the planet whose name matches passed strinmg
        closest to currentplanet - if none return currentplanet */
        {	 /*planetnum*/
            int syscount;
            
            /*planetnum*/int p=currentplanet;
            
            int d=9999;
            for(syscount=0;syscount<galsize;++syscount) {
                if (s.compareTo(galaxy[syscount].name)==0) {
                    System.out.print("***");
                    return syscount;
                    /*if (distance(galaxy[syscount],galaxy[currentplanet])<d) {
                        d=distance(galaxy[syscount],galaxy[currentplanet]);
                        p=syscount;}*/
                }
            }
            return p;
        }
        
        
        /**-Print data for given system **/
        void prisys(plansys plsy,boolean compressed) {
            if (compressed) {
                int i;
                //	  printf("\n ");
                System.out.print("" + plsy.name);
                System.out.print(" TL: "+ ((plsy.techlev)+1));
                System.out.print(" "+econnames[plsy.economy]);
                System.out.print(" "+govnames[plsy.govtype]);
            } else {
                System.out.print("\n\nSystem:  ");
                System.out.print(plsy.name);
                System.out.print("\nPosition ( "+ plsy.x);
                System.out.print(", "+plsy.y+" )");
                System.out.print("\nEconomy: ("+plsy.economy +")");
                System.out.print(econnames[plsy.economy]);
                System.out.print("\nGovernment: ("+plsy.govtype+") ");
                System.out.print(govnames[plsy.govtype]);
                System.out.print("\nTech Level: "+((plsy.techlev)+1));
                System.out.print("\nTurnover: "+(plsy.productivity));
                System.out.print("\nRadius: "+plsy.radius);
                System.out.print("\nPopulation: "+((plsy.population)>>3) +" Billion");
                
                rnd_seed = plsy.goatsoupseed;
                System.out.print("\n");
                
                goat_soup(new String("\u008F is \u0097."),plsy);
            }
        }
        /**-Various command functions **/
        
        boolean dotweakrand(String s) {
            //nativerand ^=1;
            return true;
        }
        
        boolean dolocal(String s) {	 /*planetnum*/
            int syscount;
            int d;
            //atoi(s);
            System.out.print("Galaxy number " + galaxynum);
            for(syscount=0;syscount<galsize;++syscount) {
                d=distance(galaxy[syscount],galaxy[currentplanet]);
                if(d<=maxfuel) {
                    if(d<=fuel)	System.out.print("\n * "); else System.out.print("\n - ");
                    prisys(galaxy[syscount],true);
                    System.out.print("  ("+(float)d/10 + "LY)");
                }
            }
            return true;
        }
        
        
        boolean dojump(String s) /* Jump to planet name s */
        { int d;
          /*planetnum*/int dest=matchsys(s.toUpperCase());
          if(dest==currentplanet) { System.out.print("\nBad jump"); return false; }
          d=distance(galaxy[dest],galaxy[currentplanet]);
          if (d>fuel) { System.out.print("\nJump too far"); return false; }
          fuel-=d;
          gamejump(dest);
          prisys(galaxy[currentplanet],false);
          return true;
        }
        
        boolean dosneak(String s) /* As dojump but no fuel cost */
        {
            int fuelkeep=fuel;
            boolean b;
            fuel=666;
            b=dojump(s);
            fuel=fuelkeep;
            return b;
        }
        
        boolean dogalhyp(String s) /* Jump to next galaxy */
        /* Preserve planetnum int (eg. if leave 7th planet
                arrive at 7th planet) */
        {	//(void)(&s);     /* Discard s */
            galaxynum++;
            if(galaxynum==9) {galaxynum=1;}
            buildgalaxy(galaxynum);
            return true;
        }
        
        boolean doinfo(String s) /* Info on planet */
        {	/*planetnum*/
            int dest=matchsys(s);
            prisys(galaxy[dest],false);
            return true;
        }
        
        
        boolean dohold(String s) {
            int a=(int)Integer.parseInt(s),t=0,i;
            for(i=0;i<=lasttrade;++i) {
                if ((commodities[i].units)==tonnes) t+=shipshold[i];
            }
            if(t>a) {System.out.print("\nHold too full"); return false;}
            holdspace=a-t;
            return true;
        }
        
        boolean dosell(String s) /* Sell ammount S(2) of good S(1) */
        {	int i,a,t;
                //char s2[maxlen];
                String s2 = new String();
                //spacesplit(s,s2);
                String [] elems = s.split(" ");
                s2 = elems[0];
                s = s.substring(s.indexOf(' ')+1,s.length());
                a=(int)Integer.parseInt(s);
                if (a==0) {a=1;}
                i=stringmatch(s2.toUpperCase(),tradnames,lasttrade+1);
                if(i==0) { System.out.print("\nUnknown trade good"); return false; }
                i-=1;
                
                t=gamesell(i,a);
                
                if(t==0) { System.out.print("Cannot sell any "); } else {
                    System.out.print("\nSelling "+ t);
                    System.out.print(unitnames[commodities[i].units]);
                    System.out.print(" of ");
                }
                System.out.print(tradnames[i]);
                
                return true;
                
        }
        
        
        boolean dobuy(String s) /* Buy ammount S(2) of good S(1) */
        {	int i,a,t;
                //char s2[maxlen];
                String s2 = new String();
                //spacesplit(s,s2);
                //s2 = s.split(" ")[0];
                String [] elems = s.split(" ");
                s2 = elems[0];
                s = s.substring(s.indexOf(' ')+1,s.length());;
                a=(int)Integer.parseInt(s);
                if (a==0) a=1;
                i=stringmatch(s2.toUpperCase(),tradnames,lasttrade+1);
                if(i==0) { System.out.print("\nUnknown trade good"); return false; }
                i-=1;
                
                t=gamebuy(i,a);
                if(t==0) System.out.print("Cannot buy any ");
                else {
                    System.out.print("\nBuying "+t);
                    System.out.print(unitnames[commodities[i].units]);
                    System.out.print(" of ");
                }
                System.out.print(tradnames[i]);
                return true;
        }
        
        int gamefuel(int f) /* Attempt to buy f tonnes of fuel */
        { if(f+fuel>maxfuel)  f=maxfuel-fuel;
          if(fuelcost>0) {
              if((int)f*fuelcost>cash)  f=(int)(cash/fuelcost);
          }
          fuel+=f;
          cash-=fuelcost*f;
          return f;
        }
        
        
        boolean dofuel(String s)
        /* Buy ammount S of fuel */
        {	int f=gamefuel((int)10*Integer.parseInt(s));
                if(f==0) { System.out.print("\nCan't buy any fuel");}
                System.out.print("\nBuying " + (float)f/10 + "LY of fuel");
                return true;
        }
        
        boolean docash(String s) /* Cheat alter cash by S */
        {	int a=(int)(10*Float.parseFloat(s));
                cash+=(long)a;
                if(a!=0) return true;
                System.out.print("Number not understood");
                return false;
        }
        
        boolean domkt(String s) /* Show stock market */
        { 
            displaymarket(localmarket);
            System.out.print("\nFuel :"+(float)fuel/10);
            System.out.print("      Holdspace :"+holdspace);
            return true;
        }
        
        boolean parser(String s) /* Obey command s */
        {  int i;
           
           String c = s;
           String [] elems = s.split(" ");
           c = elems[0];
           s = s.substring(s.indexOf(' ')+1,s.length());//elems[1];
           i=stringmatch(c.toUpperCase(),commands,14);
           System.out.print(c);
           System.out.println("");
           System.out.print(s);
           
           // other alternative is to replace function pointers
           // with classes
           // http://java.sun.com/developer/Books/effectivejava/Chapter5.pdf
           if(i != 0) {
               int x = i-1;
               switch(x) {               
                   case 0:  return dobuy(s);
                   case 1:  return dosell(s);
                   case 2:  return dofuel(s);
                   case 3:  return dojump(s);
                   case 4:  return docash(s);
                   case 5:  return domkt(s);
                   case 6:  return dohelp(s);
                   case 7:  return dohold(s);
                   case 8:  return dosneak(s);
                   case 9:  return dolocal(s);
                   case 10:  return doinfo(s);
                   case 11:  return dogalhyp(s);
                   case 12:  return doquit(s);
                   case 13:  return dotweakrand(s);
                   default:
                       return false;
               }
               
           } else {
               System.out.print("\n Bad command :" + s + " + number:" + i);
           }

           return false;
        }
        
        
        boolean doquit(String s) {
           /* (void)(&s);
            exit(0);*/
            return false;
        }
        
        boolean dohelp(String s) {
            
            System.out.print("\nCommands are:");
            System.out.print("\nBuy   tradegood ammount");
            System.out.print("\nSell  tradegood ammount");
            System.out.print("\nFuel  ammount    (buy ammount LY of fuel)");
            System.out.print("\nJump  planetname (limited by fuel)");
            System.out.print("\nSneak planetname (any distance - no fuel cost)");
            System.out.print("\nGalhyp           (jumps to next galaxy)");
            System.out.print("\nInfo  planetname (prints info on system");
            System.out.print("\nMkt              (shows market prices)");
            System.out.print("\nLocal            (lists systems within 7 light years)");
            System.out.print("\nCash number      (alters cash - cheating!)");
            System.out.print("\nHold number      (change cargo bay)");
            System.out.print("\nQuit or ^C       (exit)");
            System.out.print("\nHelp             (display this text)");
            System.out.print("\nRand             (toggle RNG)");
            System.out.print("\n\nAbbreviations allowed eg. b fo 5 = Buy Food 5, m= Mkt");
            return true;
        }
        
        
/* "Goat Soup" planetary description string code - adapted from Christian Pinder's
  reverse engineered sources. */
        
        public class desc_choice {
            desc_choice(String s0,String s1, String s2, String s3, String s4) {
                option[0] = new String(s0);
                option[1] = new String(s1);
                option[2] = new String(s2);
                option[3] = new String(s3);
                option[4] = new String(s4);
            }
            String []option = new String[5];
        };
        
        desc_choice [] desc_list =
        {
            /* 81 */	new desc_choice("fabled", "notable", "well known", "famous", "noted"),
            /* 82 */	new desc_choice("very", "mildly", "most", "reasonably", ""),
            /* 83 */	new desc_choice("ancient", "\u0095", "great", "vast", "pink"),
            /* 84 */	new desc_choice("\u009E \u009D plantations", "mountains", "\u009C", "\u0094 forests", "oceans"),
            /* 85 */	new desc_choice("shyness", "silliness", "mating traditions", "loathing of \u0086", "love for \u0086"),
            /* 86 */	new desc_choice("food blenders", "tourists", "poetry", "discos", "\u008E"),
            /* 87 */	new desc_choice("talking tree", "crab", "bat", "lobst", "\u00B2"),
            /* 88 */	new desc_choice("beset", "plagued", "ravaged", "cursed", "scourged"),
            /* 89 */	new desc_choice("\u0096 civil war", "\u009B \u0098 \u0099s", "a \u009B disease", "\u0096 earthquakes", "\u0096 solar activity"),
            /* 8A */	new desc_choice("its \u0083 \u0084", "the \u00B1 \u0098 \u0099","its inhabitants' \u009A \u0085", "\u00A1", "its \u008D \u008E"),
            /* 8B */	new desc_choice("juice", "brandy", "water", "brew", "gargle blasters"),
            /* 8C */	new desc_choice("\u00B2", "\u00B1 \u0099", "\u00B1 \u00B2", "\u00B1 \u009B", "\u009B \u00B2"),
            /* 8D */	new desc_choice("fabulous", "exotic", "hoopy", "unusual", "exciting"),
            /* 8E */	new desc_choice("cuisine", "night life", "casinos", "sit coms", " \u00A1 "),
            /* 8F */	new desc_choice("\u00B0", "The planet \u00B0", "The world \u00B0", "This planet", "This world"),
            /* 90 */	new desc_choice("n unremarkable", " boring", " dull", " tedious", " revolting"),
            /* 91 */	new desc_choice("planet", "world", "place", "little planet", "dump"),
            /* 92 */	new desc_choice("wasp", "moth", "grub", "ant", "\u00B2"),
            /* 93 */	new desc_choice("poet", "arts graduate", "yak", "snail", "slug"),
            /* 94 */	new desc_choice("tropical", "dense", "rain", "impenetrable", "exuberant"),
            /* 95 */	new desc_choice("funny", "wierd", "unusual", "strange", "peculiar"),
            /* 96 */	new desc_choice("frequent", "occasional", "unpredictable", "dreadful", "deadly"),
            /* 97 */	new desc_choice("\u0082 \u0081 for \u008A", "\u0082 \u0081 for \u008A and \u008A", "\u0088 by \u0089", "\u0082 \u0081 for \u008A but \u0088 by \u0089","a\u0090 \u0091"),
            /* 98 */	new desc_choice("\u009B", "mountain", "edible", "tree", "spotted"),
            /* 99 */	new desc_choice("\u009F", "\u00A0", "\u0087oid", "\u0093", "\u0092"),
            /* 9A */	new desc_choice("ancient", "exceptional", "eccentric", "ingrained", "\u0095"),
            /* 9B */	new desc_choice("killer", "deadly", "evil", "lethal", "vicious"),
            /* 9C */	new desc_choice("parking meters", "dust clouds", "ice bergs", "rock formations", "volcanoes"),
            /* 9D */	new desc_choice("plant", "tulip", "banana", "corn", "\u00B2weed"),
            /* 9E */	new desc_choice("\u00B2", "\u00B1 \u00B2", "\u00B1 \u009B", "inhabitant", "\u00B1 \u00B2"),
            /* 9F */	new desc_choice("shrew", "beast", "bison", "snake", "wolf"),
            /* A0 */	new desc_choice("leopard", "cat", "monkey", "goat", "fish"),
            /* A1 */	new desc_choice("\u008C \u008B", "\u00B1 \u009F \u00A2","its \u008D \u00A0 \u00A2", "\u00A3 \u00A4", "\u008C \u008B"),
            /* A2 */	new desc_choice("meat", "cutlet", "steak", "burgers", "soup"),
            /* A3 */	new desc_choice("ice", "mud", "Zero-G", "vacuum", "\u00B1 ultra"),
            /* A4 */	new desc_choice("hockey", "cricket", "karate", "polo", "tennis")
        };
        
/* B0 = <planet name>
         B1 = <planet name>ian
         B2 = <random name>
 */
        
        int gen_rnd_number() {
            int a,x;
            x = (int)((rnd_seed.a * 2) & 0xFF);
            a = x + rnd_seed.c;
            if (rnd_seed.a > 127)	a++;
            rnd_seed.a = (char)(a & 0xFF);
            rnd_seed.c = (char)x;
            
            a = a / 256;	/* a = any carry left from above */
            x = (int)rnd_seed.b;
            a = (a + x + rnd_seed.d) & 0xFF;
            rnd_seed.b = (char)a;
            rnd_seed.d = (char)x;
            return a;
        }
        
        void goat_print(int c) {
            char[] n = new char[1];
            n[0] = (char)c;
            String s = new String(n);
            System.out.print(s);
        }
        
        void goat_soup(String source,plansys psy) {
            int indx =0;
            for(;;) {
                if( indx >= source.length())break;
                int c=(int)source.charAt(indx++);
                if(c=='\0')	break;
                if(c<0x80) {goat_print(c);} else {
                    if (c <=0xA4) {
                        int rnd = gen_rnd_number();
                        
                        goat_soup(desc_list[c-0x81].option[(int)((rnd >= 0x33?1:0)+(rnd >= 0x66?1:0)+(rnd >= 0x99?1:0)+(rnd >= 0xCC?1:0))],psy);
                    } else switch(c) {
                        case 0xB0: /* planet name */
                        {
                            int i=1;
                            goat_print(psy.name.charAt(0));
                            while(i < psy.name.length()) goat_print(tolower(psy.name.charAt(i++)));
                        }           break;
                        case 0xB1: /* <planet name>ian */
                        { int i=1;
                          goat_print(psy.tname[0]);
                          while(i < psy.name.length()) {
                              if((i+1 < psy.name.length()) || ((psy.name.charAt(i)!='E')	&& (psy.name.charAt(i)!='I')))
                                  goat_print(tolower(psy.name.charAt(i)));
                              i++;
                          }
                          System.out.print("ian");
                        }	break;
                        case 0xB2: /* random name */
                        {	int i;
                                int len = gen_rnd_number() & 3;
                                for(i=0;i<=len;i++) {
                                    int x = gen_rnd_number() & 0x3e;
                                    if( x > pairs0.length())break;
                                    if(pairs0.charAt(x)!='.') goat_print(pairs0.charAt(x));
                                    if(i!=0 && (pairs0.charAt(x+1)!='.')) goat_print(pairs0.charAt(x+1));
                                }
                        }	break;
                        default: System.out.print("<bad char in data [" + c +"]>"); return;
                    }	/* endswitch */
                }	/* endelse */
            }	/* endwhile */
        }/* endfunc */
        
        private BufferedReader stdin = new BufferedReader( new InputStreamReader( System.in ));
        
        public void run() {
            int i;
            
            nativerand=true;
            System.out.print("\nWelcome to Text Elite 1.4.\n");
            
            for(i=0;i<lasttrade;i++)
                tradnames[i]=commodities[i].name;
            
            mysrand(12345);/* Ensure repeatability */
            
            galaxynum=1;	buildgalaxy(galaxynum);
            
            currentplanet=numforLave;                        /* Don't use jump */
            localmarket = genmarket(0x00,galaxy[numforLave]);/* Since want seed=0 */
            
            fuel=maxfuel;
            
            //#define PARSER(S) { char buf[0x10];strcpy(buf,S);parser(buf);}
            
            //parser("hold 20");         /* Small cargo bay */
            holdspace = 20;
            cash = 1000;
            //parser("cash +100");       /* 100 CR */
            parser("help");
            
            //#undef PARSER
            
            for(;;) {
                System.out.print("\n\nCash :"+((float)cash)/10 + ">");
                
                try{
                    parser(stdin.readLine());
                    
                } catch(java.io.IOException e) {
                    break;
                }
            }
            
            
   /* 6502 Elite fires up at Lave with fluctuation=00
      and these prices tally with the NES ones.
      However, the availabilities reside in the saved game data.
      Availabilities are calculated (and fluctuation randomised)
      on hyperspacing
      I have checked with this code for Zaonce with fluctaution &AB
      against the SuperVision 6502 code and both prices and availabilities tally.
    */
            // return;
        }
        
        
        public void run2() {
            String s1 = "Hello World with spaces";
            String s2 = "not much";// = new String();
            String s3 = s1;
            if( s1.compareTo(s3) == 0)
                System.out.println("Strings match");
            //s1.
            //MyStringUtil utl =new MyStringUtil(s1,s2);
            //utl.spacesplit();
            //s1 = utl.s;
            //s2 = utl.t;
         /*  s2 = s1.split(" ")[0];
           s1 = s1.split(" ")[1]+s1.split(" ")[2] + s1.split(" ")[3];
          
           //s2 = s1;
           //stripout(s1, ' ');
           //s1 = s1.replaceAll(" ", "");
          
            // stringbeg test
           if( stringbeg(s2,s1) == false)System.out.println("stringbeg works");
          
            System.out.println("s1: "+s1);
            System.out.println("s2: "+s2);
          
          
            // stringmatch test
            String [] strs = { "hello", "goodbye", "howdy" };
          
            String p = "hello";
          
            if( stringmatch(p,strs,3)-1==0)System.out.println("stringmatch works");
          */
            
            
            /* Split string test*/
           /* String [] elems = s1.split(" ");
            String c;
            c = elems[0];
            s1 = s1.substring(s1.indexOf(' ')+1,s1.length());//elems[1];
            
            System.out.println("s1: "+s1.toUpperCase().toUpperCase());
            System.out.println("c: "+c +"s s");*/
            
            
            /* goat soup test*/
            String cc;// = new String();
            //cc = "xA3";
            char val = '\u00A7';
            char [] arr3 = new char[1];
            arr3[0] = val;
            cc = new String(arr3);
            int newval = (int)val;//nteger.parseInt(val);
            
            if( val ==0xA7 ) {
                System.out.print("passed test\n");
            } else {
                System.out.print("char is" + newval);
            }
            
            
            String bigTest = "\u009E \u009D plantations";
            
            System.out.println(bigTest);
            
            System.out.println("First char:"+ bigTest.charAt(0));
        }
        
    }
    
    
    
    
    
    
    /** Creates a new instance of Main */
    public Main() {
        elite el = new elite();
        el.run();
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        
        
        try {
            Main mn = new Main();
        } catch(NullPointerException e) {
            System.out.println(e.toString());
            
        }
        System.out.println("\nHello World\n");
    }
    
}
