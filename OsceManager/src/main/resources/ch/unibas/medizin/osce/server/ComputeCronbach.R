
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
                       "range",   "r",".","character",
                       "clean",   "c",  0,"logical",
                       "help",    "h",  0,"logical"
                      ),
                     ncol=4,byrow=TRUE
                    )
             )
   return(c(opt$filename,opt$range))
}


.validateArg <- function(x){

  filename<-x[1]
  range   <-x[2]
 
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

  return(c(filename,rangeLeft,rangeRight))

}


.checkArg()->x

.validateArg(x)->x

filename  <-x[1]
rangeLeft <-as.integer(x[2])
rangeRight<-as.integer(x[3])

df<-read.table(filename,header=T,sep="|",na.strings=c("NA","na","NULL","#NULL!"),dec=".")

nRow<-nrow(df)
nCol<-ncol(df)

df<-df[3:(nCol-1)]
nCol<-ncol(df)

df.metrics <-data.frame()

z<-cronbach(df)

df.metrics[1,1]<-"number of sample units"
df.metrics[2,1]<-"number of items"
df.metrics[3,1]<-"cronbach's alpha"
df.metrics[1,2]<-z$sample.size
df.metrics[2,2]<-z$number.of.items
df.metrics[3,2]<-round(z$alpha,digits=4)

df.cronbachs<-data.frame()

print(paste(df.metrics[3,2]));

for(i in 1:nCol){
  df.cronbachs[i,1]<-names(df[i])
  z<-cronbach(df[-i])
  df.cronbachs[i,2]<-round(z$alpha,digits=4)
    
  print(paste(df.cronbachs[i,1]));
  print(paste(df.cronbachs[i,2]));
}

.endOfProgram()