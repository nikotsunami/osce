
####################################
## libraries
####################################

library(getopt)
#library(RMySQL)
library(psy)


####################################
## global vars
####################################

prgName<-"ComputeResult.R"
#dirToStore<-"/tmp/"
#dirToStore<-"E:/OSCE Manager/LEARN_R/OsceData/"
#fileImageToStore   <-paste(dirToStore,"output-graphics.png",sep="")
#fileDataToStore    <-paste(dirToStore,"output-data.csv",sep="")
#fileMetricsToStore <-paste(dirToStore,"output-metrics.csv",sep="")

.checkArg <- function(){
 
   opt<-getopt(matrix(c(
                       "filename","f",".","character",
                       "missing", "m",".","list",
                       "passmark","p",".","character",
                       "range",   "r",".","character",
                       "clean",   "c",  0,"logical",
                       "help",    "h",  0,"logical"
                      ),
                     ncol=4,byrow=TRUE
                    )
             )
   return(c(opt$filename,opt$range,opt$missing,opt$passmark))
}


.validateArg <- function(x){

  filename<-x[1]
  range   <-x[2]
  missing <-x[3]
  passmark<-x[4]
 
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

  return(c(filename,rangeLeft,rangeRight,missing,passmark))

}

####################################
## errorMessage
####################################

.errorMessage <- function(message){

  # STDOUT
  cat("\n")
  cat(paste("  Error executing ",prgName,":\n",sep=""))
  cat("   ",message,"\n")

  # exit
  cat("\n")
  cat("  ...exit\n")
  cat("\n")
  q()

}

####################################
## checkData
####################################

.checkData <- function(x){

  filename  <-x[1]
  rangeLeft <-as.integer(x[2])
  rangeRight<-as.integer(x[3])

  if(!all(complete.cases(df))){
    .errorMessage(paste("the dataset '",filename,"' contains missing value(s)",sep=""))
  }

  if(!all(sapply(df[,3:nCol],is.numeric))){
    .errorMessage(paste("non-numeric values for some items in '",filename,"'",sep=""))
  }

  if(!all(df[,nCol]>=rangeLeft) || !all(df[,nCol]<=rangeRight)){
    .errorMessage("some impression's codes are not in range")
  }

  if(!all(df[,nCol]%%1==0)){
    .errorMessage("some impression's codes are not whole numbers")
  }

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
passMark  <- as.double(x[5])

df<-read.table(filename,header=T,sep="|",na.strings=c("NA","na","NULL","#NULL!"),dec=".")

nRow<-nrow(df)
nCol<-ncol(df)

cat("\n")
cat("  analysing data set: a table of",nRow,"rows and",nCol,"columns\n")

.checkData(x)

cat("\n")
cat(paste("  the range for impression's code: [",rangeLeft,";",rangeRight,"]\n",sep=""))

rangeAverage<-round(rangeLeft+(rangeRight-rangeLeft)/2,1)

cat("\n")
cat("  computing pass mark based on the average impression's code",rangeAverage,"\n")

testColName <- c(missing[[1]])

cat("testColName : ", testColName, "\n")
df1 <- df[3:(nCol-1)]

if (0 %in% testColName){
	y<-rowSums(df[3:(nCol-1)])
	
	overAllMean = round(sum(mean(df1)) ,digits=2)
	overAllSd = round(sum(sd(df1)) ,digits=2)
} else{

	testCol<-which(names(df) %in% testColName)
	
	if (length(testCol) != 0)
	{	
		y<-rowSums(df1[-testCol])
	}
	else
	{
		y<-rowSums(df[3:(nCol-1)])
	}
	
	overAllMean = round(sum(mean(df1[-testCol])) ,digits=2)
	overAllSd = round(sum(sd(df1[-testCol])) ,digits=2)
}

# computing linear model (linear regression)
#x<-df[,nCol]
#z<-lm(y~x)
#beta0<-z$coefficients[1]
#beta1<-z$coefficients[2]

# computing pass mark, pass points, and fall points
#passMark<-2*beta1+beta0

cat("\n")
cat(paste("  updating input data (s. file '",filename,"')\n",sep=""))

df[y>=passMark,nCol+1]<-"pass"
df[y< passMark,nCol+1]<-"fall"

df[y>=passMark,nCol+1]<-"pass"
df[y< passMark,nCol+1]<-"fall"

print(paste(length(df[y>=passMark,nCol+1])))
print(paste(length(df[y< passMark,nCol+1])))
print(paste(round(passMark ,digits=2)))
print(paste(min(y)))
print(paste(max(y)))
print(paste(overAllMean))
print(paste(overAllSd))

.endOfProgram()