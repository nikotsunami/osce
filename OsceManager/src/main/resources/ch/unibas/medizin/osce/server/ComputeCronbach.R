####################################
## libraries
####################################

library(getopt)
#library(RMySQL)
library(psy)

####################################
## global vars
####################################

 

prgName<-"compute-station-metrics.R"
#dirToStore<-"/tmp/"
dirToStore<-"E:/OSCE Manager/LEARN_R/OsceData/"
fileImageToStore   <-paste(dirToStore,"output-graphics.png",sep="")
fileDataToStore    <-paste(dirToStore,"output-data.csv",sep="")
fileMetricsToStore <-paste(dirToStore,"output-metrics.csv",sep="")

.checkArg <- function(){
   opt<-getopt(matrix(c(
                       "filename","f",".","character",
                       "missing", "m",".","list",
                       "range",   "r",".","character",
                       "clean",   "c",  0,"logical",
                       "help",    "h",  0,"logical"
                      ),
                     ncol=4,byrow=TRUE
                    )
             )
   return(c(opt$filename,opt$range,opt$missing))
}

.validateArg <- function(x){
  filename<-x[1]
  range   <-x[2]
  missing <-x[3]

  if(grepl("^[[:digit:]]{1,2}[.]{2}[[:digit:]]{1,2}$",range,perl=TRUE)){
    y<-as.integer(unlist(strsplit(range,"\\.\\.")[1]))
    rangeLeft <-y[1]
    rangeRight<-y[2]

    if(rangeLeft>=rangeRight){
      .errorMessage(paste("invalid range '",range,"'",sep=""))
    }
  }

  y<-paste("grep -c '|'",filename)

  if(y=="0"){
    .errorMessage(paste("can't find the proper field delimiter in '",filename,"'",sep=""))
  }

  return(c(filename,rangeLeft,rangeRight,missing))
}

####################################
## endOfProgram
####################################

.endOfProgram <- function(){
  cat("\n")
  cat("  ...done\n")
  cat("\n")
  q()
}

####################################
## Main
####################################

.checkArg()->x

.validateArg(x)->x

filename  <-x[1]
rangeLeft <-as.integer(x[2])
rangeRight<-as.integer(x[3])
missing   <- strsplit(x[4], ",")

df<-read.table(filename,header=T,sep="|",na.strings=c("NA","na","NULL","#NULL!"),dec=".")

nRow<-nrow(df)
nCol<-ncol(df)

df1<-df[5:(nCol-1)]
df<-df[5:(nCol-1)]
df2 <- df1
nCol<-ncol(df)

df.metrics <-data.frame()

testColName <- c(missing[[1]])

if (0 %in% testColName){
        z<-cronbach(df)
        overAllMean = round(mean(mean(df[!is.na(df)])) ,digits=2)
        overAllSd = round(sd(sd(df)) ,digits=2)
} else{
        testCol<-which(names(df) %in% testColName)

        if (length(testCol) != 0)
        {
                z<-cronbach(df1[-testCol])
                overAllMean = round(mean(mean(df1[-testCol])) ,digits=2)
                overAllSd = round(sd(sd(df1[-testCol])) ,digits=2)
        }
        else
        {
                z<-cronbach(df)
                overAllMean = round(mean(mean(df[!is.na(df)])) ,digits=2)
                overAllSd = round(sd(sd(df)) ,digits=2)
        }
}

 

df.metrics[1,1]<-"number of sample units"
df.metrics[2,1]<-"number of items"
df.metrics[3,1]<-"cronbach's alpha"
df.metrics[1,2]<-z$sample.size
df.metrics[2,2]<-z$number.of.items
df.metrics[3,2]<-round(z$alpha,digits=4)

df.cronbachs<-data.frame()

#sdDf <- sapply(df1, FUN=sd)
#cat(sdDf, "\n")

print(paste(df.metrics[3,2]));
print(paste(overAllMean));
print(paste(overAllSd));

for(i in 1:nCol){
  df.cronbachs[i,1]<-names(df[i])
	
       if (0 %in% testColName)
        {
 					z<-cronbach(df[-i])
        }
        else
        {
					z<-cronbach(df2[c(-testCol, -i)])
        }

  df.cronbachs[i,2]<-round(z$alpha,digits=3)

  print(paste(df.cronbachs[i,1]));
  print(paste(df.cronbachs[i,2]));
  print(paste( round(mean(df[!is.na(df[i]),i]) ,digits = 2) ));
  print(paste( round(sd(df[!is.na(df[i]),i]) ,digits = 2) ));
}

.endOfProgram()
