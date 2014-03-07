package org.springframework.cloud.service;

import org.springframework.util.StringUtils;

/**
 * Configuration for pooling
 * 
 * @author Ramnivas Laddad
 * @author Mark Fisher
 * @author Thomas Risberg
 *
 */
public class PooledServiceConnectorConfig implements ServiceConnectorConfig {
	private PoolConfig poolConfig;

	public PooledServiceConnectorConfig(PoolConfig poolConfig) {
		this.poolConfig = poolConfig;
	}

	public PoolConfig getPoolConfig() {
		return poolConfig;
	}
	
	public static class PoolConfig {
		private int minPoolSize;
		private int maxPoolSize;
		private int maxWaitTime;

		public PoolConfig(int minPoolSize, int maxPoolSize, int maxWaitTime) {
			this.minPoolSize = minPoolSize;
			this.maxPoolSize = maxPoolSize;
			this.maxWaitTime = maxWaitTime;
		}
		
		public PoolConfig(int maxPoolSize, int maxWaitTime) {
			this(0, maxPoolSize, maxWaitTime);
		}		
		
		public PoolConfig(String poolSize, int maxWaitTime) {
			determinePoolSizeRange(poolSize);
			this.maxWaitTime = maxWaitTime;
		}
		
		/**
		 * Getter corresponding to the DBCP initialSize property
		 */
		public int getInitialSize() {
			return minPoolSize;
		}

		/**
		 * Getter corresponding to the DBCP minIdle property
		 */
		public int getMinIdle() {
			return minPoolSize;
		}

		/**
		 * Getter corresponding to the DBCP maxActive property
		 */
		public int getMaxActive() {
			return maxPoolSize;
		}

		// For commons-pool2
        public int getMaxTotal() {
            return maxPoolSize;
        }

        /**
		 * Getter corresponding to the DBCP maxWait property
		 */
		public int getMaxWait() {
			return maxWaitTime;
		}

        // For commons-pool2		
        public int getMaxWaitMillis() {
            return maxWaitTime;
        }
		
		private void determinePoolSizeRange(String poolSize) {
			if (StringUtils.hasText(poolSize)) {
				try {
					int minPoolSize;
					int maxPoolSize;
					int separatorIndex = poolSize.indexOf('-');
					if (separatorIndex != -1) {
						minPoolSize = Integer.valueOf(poolSize.substring(0, separatorIndex));
						maxPoolSize = Integer.valueOf(poolSize.substring(separatorIndex + 1, poolSize.length()));
						if (minPoolSize > maxPoolSize) {
							throw new IllegalArgumentException(
									"Lower bound of pool-size range must not exceed the upper bound");
						}
					} else {
						Integer value = Integer.valueOf(poolSize);
						minPoolSize = 0;
						maxPoolSize = value;
					}
					this.minPoolSize = Integer.valueOf(minPoolSize);
					this.maxPoolSize = Integer.valueOf(maxPoolSize);
				} catch (NumberFormatException ex) {
					throw new IllegalArgumentException("Invalid pool-size value [" + poolSize + "]: only single " +
							"maximum integer (e.g. \"5\") and minimum-maximum range (e.g. \"3-5\") are supported", ex);
				}
			}
		}
	}
}
